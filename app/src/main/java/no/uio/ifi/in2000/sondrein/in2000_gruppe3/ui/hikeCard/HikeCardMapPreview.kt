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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R

@Composable
fun HikeCardMapPreview(feature: Feature) {
    val coordinates = mutableListOf<Point>()

    feature.geometry.coordinates.forEach { coordList ->
        coordList.forEach { coord ->
            coordinates.add(Point.fromLngLat(coord[1], coord[0]))
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
        width = 400,
        height = 200,
        lineCoordinates = coordinates
    )

    Log.d("HikeCardMapPreview", "Static map URL: $staticMapUrl")

    Surface(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Use AsyncImage to load the static map
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
    width: Int,
    height: Int,
    lineCoordinates: List<Point>
): String {
    // Limit number of coordinates to avoid exceeding URL length limits
    val simplifiedCoordinates = if (lineCoordinates.size > 100) {
        simplifyPath(lineCoordinates, 100)
    } else {
        lineCoordinates
    }

    // Construct path for the polyline
    val pathString = StringBuilder()
    if (simplifiedCoordinates.isNotEmpty()) {
        pathString.append("path-5+FF3300-1.0(")
        simplifiedCoordinates.forEachIndexed { index, point ->
            if (index > 0) pathString.append(",")
            pathString.append("${point.longitude()},${point.latitude()}")
        }
        pathString.append(")")
    }

    // Legg til markør ved startpunktet
    val startPoint = simplifiedCoordinates.first()
    pathString.append(",pin-s+FF0000(${startPoint.longitude()},${startPoint.latitude()})")

    // Build the static map URL
    return "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/" +
            "${pathString}/" +
            "${center.longitude()},${center.latitude()},$zoom,0/" +
            "${width}x${height}@2x" +
            "?access_token=${BuildConfig.MAPBOX_SECRET_TOKEN}"
}

// Helper function to simplify a path to a maximum number of points
private fun simplifyPath(points: List<Point>, maxPoints: Int): List<Point> {
    if (points.size <= maxPoints) return points

    val step = points.size / maxPoints
    return points.filterIndexed { index, _ -> index % step == 0 }
        .take(maxPoints)
}

// Calculate an ideal zoom level based on the bounding box
private fun calculateIdealZoom(bbox: BoundingBox): Double {
    val latDiff = bbox.north() - bbox.south()
    val lngDiff = bbox.east() - bbox.west()
    val maxDiff = maxOf(latDiff, lngDiff)

    return when {
        maxDiff > 5.0 -> 2.0
        maxDiff > 1.0 -> 5.0
        maxDiff > 0.5 -> 7.0
        maxDiff > 0.1 -> 10.0
        maxDiff > 0.05 -> 12.0
        maxDiff > 0.01 -> 13.0
        else -> 14.0
    }
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

    val buffer = 0.05 * maxOf(maxLat - minLat, maxLng - minLng)
    return BoundingBox.fromLngLats(
        minLng - buffer,
        minLat - buffer,
        maxLng + buffer,
        maxLat + buffer
    )
}