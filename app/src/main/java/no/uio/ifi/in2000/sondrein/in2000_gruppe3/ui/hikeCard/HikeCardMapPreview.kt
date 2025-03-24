package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.hikeCard

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenUIState
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun HikeCardMapPreview(
    homeScreenViewModel: HomeScreenViewModel,
    feature: Feature
) {
    val coordinates = mutableListOf<Point>()
    val uiState by homeScreenViewModel.homeScreenUIState.collectAsState()

    feature.geometry.coordinates.forEach { coordList ->
        coordList.forEach { coord ->
            if (coord.size >= 2) {
                coordinates.add(Point.fromLngLat(coord[1], coord[0]))
            }
        }
    }

    // Calculate the center point of the bounding box
    val bbox = getBoundingBox(coordinates)
    val center = Point.fromLngLat(
        (bbox.west() + bbox.east()) / 2,
        (bbox.north() + bbox.south()) / 2
    )

    // Calculate zoom level based on the bounding box
    val zoom = calculateIdealZoom(bbox)

    // Create a static map URL
    val staticMapUrl = createStaticMapUrl(
        center = center,
        zoom = zoom,
        lineCoordinates = coordinates,
        uiState = uiState,
        feature = feature
    )

    Log.d("HikeCardMapPreview", "Static map URL: $staticMapUrl")

    Surface(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Tegner kartet som et bilde
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(staticMapUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Map preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            error = painterResource(id = R.drawable.caution)
        )
    }
}

// Create a static map URL for a given set of coordinates
private fun createStaticMapUrl(
    center: Point,
    zoom: Double,
    lineCoordinates: List<Point>,
    uiState: HomeScreenUIState,
    feature: Feature
): String {
    // Limit number of coordinates to avoid exceeding URL length limits
    val simplifiedCoordinates = if (lineCoordinates.size > 100) {
        simplifyPath(lineCoordinates, 100)
    } else {
        lineCoordinates
    }

    // Build the path string directly from coordinates
    val polyline = encodePolyline(simplifiedCoordinates)
    val encodedPolyline = java.net.URLEncoder.encode(polyline, "UTF-8")

    // Markør for start- og sluttpunkt
    // Er ikke sikkert dette er riktig punkter
    val startPoint = simplifiedCoordinates.first()
    val endPoint = simplifiedCoordinates.last()
    val markers = "pin-s+4285F4(${startPoint.longitude()},${startPoint.latitude()})," +
            "pin-s+FF0000(${endPoint.longitude()},${endPoint.latitude()})"

    // Velger mapstyle
    val mapStyle = uiState.mapStyle
    val darkmode = uiState.mapIsDarkmode
    var mapStyleUrl = when (mapStyle) {
        "STANDARD" -> "streets-v11"
        "SATELLITE" -> "satellite-v9"
        "OUTDOORS" -> "outdoors-v12"
        else -> "streets-v11"
    }
    if (mapStyle == "STANDARD" && darkmode) {
        mapStyleUrl = "dark-v10"
    }

    // Turens farge
    val color = rgbaToHex(feature.color)

    /**
     * Legge til layer på kartet fungerer kanskje for å tegne turen
     * &addlayer={"id":"road-overlay","type":"line","source":"composite","source-layer":"road","filter":["==",["get","class"],"motorway"],"paint":{"line-color":"%23ff0000","line-width":5}}&before_layer=road-label
     */

    Log.d("HikeCardMapPreview", "Path string: $encodedPolyline")

    // Build the static map URL with the path
    return "https://api.mapbox.com/styles/v1/mapbox/${mapStyleUrl}/static/" +
            "path-6+${color}-1($encodedPolyline),${markers}/" +
            "${center.longitude()},${center.latitude()},$zoom,0,0/" +
            "600x500@2x" + // widthxheight@2x
            "?access_token=${BuildConfig.MAPBOX_SECRET_TOKEN}" +
            "&attribution=false&logo=false" // Remove attribution and logo
}

// Convert an RGBA color string to a hex color string
private fun rgbaToHex(rgba: String): String {
    val regex = Regex("""rgba\((\d+),\s*(\d+),\s*(\d+),\s*([\d.]+)\)""")
    val matchResult = regex.find(rgba)
    return if (matchResult != null) {
        val (r, g, b, a) = matchResult.destructured
        String.format("%02X%02X%02X", r.toInt(), g.toInt(), b.toInt())
    } else {
        throw IllegalArgumentException("Invalid RGBA format")
    }
}

private fun encodePolyline(points: List<Point>): String {
    return com.mapbox.geojson.utils.PolylineUtils.encode(
        points.map { com.mapbox.geojson.Point.fromLngLat(it.longitude(), it.latitude()) },
        5 // precision
    )
}

// Helper function to simplify a path to a maximum number of points
private fun simplifyPath(points: List<Point>, maxPoints: Int): List<Point> {
    if (points.size <= maxPoints) return points

    // Always include first and last points
    val result = mutableListOf(points.first())

    // Calculate how many intermediate points we can include
    val intermediatePoints = maxPoints - 2
    if (intermediatePoints <= 0) return listOf(points.first(), points.last())

    // Find critical turning points by measuring distance or angle changes
    val pointsWithoutEndpoints = points.subList(1, points.size - 1)
    val step = pointsWithoutEndpoints.size / intermediatePoints

    for (i in 0 until intermediatePoints) {
        val index = (i * step).coerceAtMost(pointsWithoutEndpoints.size - 1)
        result.add(pointsWithoutEndpoints[index])
    }

    result.add(points.last())
    return result
}

// Calculate the bounding box of a set of coordinates
private fun getBoundingBox(points: List<Point>): BoundingBox {
    if (points.isEmpty()) return BoundingBox.fromLngLats(0.0, 0.0, 0.0, 0.0)

    var minLat = 90.0
    var maxLat = -90.0
    var minLng = 180.0
    var maxLng = -180.0

    points.forEach { point ->
        minLat = minOf(minLat, point.latitude())
        maxLat = maxOf(maxLat, point.latitude())
        minLng = minOf(minLng, point.longitude())
        maxLng = maxOf(maxLng, point.longitude())
    }

    // Legg til en buffer basert på størrelsen på bounding box
    val latBuffer = (maxLat - minLat) * 0.5
    val lngBuffer = (maxLng - minLng) * 0.5

    return BoundingBox.fromLngLats(
        minLng - lngBuffer,
        minLat - latBuffer,
        maxLng + lngBuffer,
        maxLat + latBuffer
    )
}

// Calculate an ideal zoom level based on the bounding box
private fun calculateIdealZoom(bbox: BoundingBox): Double {
    val latDiff = bbox.north() - bbox.south()
    val lngDiff = bbox.east() - bbox.west()

    // Ensure we have a minimum difference to prevent extreme zoom levels
    val maxDiff = maxOf(latDiff, lngDiff, 0.005)

    // More gradual zoom scaling
    return when {
        maxDiff > 10.0 -> 5.0
        maxDiff > 5.0 -> 7.0
        maxDiff > 2.0 -> 9.0
        maxDiff > 1.0 -> 10.0
        maxDiff > 0.5 -> 10.0
        maxDiff > 0.2 -> 10.0
        maxDiff > 0.1 -> 11.0
        maxDiff > 0.05 -> 12.0
        maxDiff > 0.01 -> 13.0
        else -> 13.0
    }
}
