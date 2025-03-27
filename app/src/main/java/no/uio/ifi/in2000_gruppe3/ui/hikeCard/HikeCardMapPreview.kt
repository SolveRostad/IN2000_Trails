package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import no.uio.ifi.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxUIState
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

@Composable
fun HikeCardMapPreview(
    mapboxViewModel: MapboxViewModel,
    feature: Feature
) {
    val coordinates = mutableListOf<Point>()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    feature.geometry.coordinates.forEach { coordinate ->
        coordinates.add(Point.fromLngLat(coordinate[0], coordinate[1]))
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
        uiState = mapboxUIState,
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
    uiState: MapboxUIState,
    feature: Feature
): String {
    val polyline = encodePolyline(lineCoordinates)
    val encodedPolyline = java.net.URLEncoder.encode(polyline, "UTF-8")

    val startPoint = lineCoordinates.first()
    val endPoint = lineCoordinates.last()
    val markers = "pin-s+4285F4(${startPoint.longitude()},${startPoint.latitude()})," +
            "pin-s+FF0000(${endPoint.longitude()},${endPoint.latitude()})"

    val mapStyle = uiState.mapStyle
    var mapStyleUrl = when (mapStyle) {
        Style.STANDARD_SATELLITE -> "satellite-v9"
        else -> "outdoors-v12"
    }

    val color = colorToHex(feature.color!!)
    return "https://api.mapbox.com/styles/v1/mapbox/${mapStyleUrl}/static/" +
            "path-6+${color}-1($encodedPolyline),${markers}/" +
            "${center.longitude()},${center.latitude()},$zoom,0,0/" +
            "600x500@2x" + // widthxheight@2x
            "?access_token=${BuildConfig.MAPBOX_SECRET_TOKEN}" +
            "&attribution=false&logo=false" // Remove attribution and logo
}

private fun colorToHex(color: Color): String {
    return String.format(
        "%02x%02x%02x",
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )
}

private fun encodePolyline(points: List<Point>): String {
    return com.mapbox.geojson.utils.PolylineUtils.encode(
        points.map { com.mapbox.geojson.Point.fromLngLat(it.longitude(), it.latitude()) },
        5 // precision
    )
}

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
    val latBuffer = (maxLat - minLat)
    val lngBuffer = (maxLng - minLng)

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
