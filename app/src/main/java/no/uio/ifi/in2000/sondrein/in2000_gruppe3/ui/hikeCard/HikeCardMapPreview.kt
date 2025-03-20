package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.hikeCard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.rememberMapState
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.plugin.gestures.generated.GesturesSettings
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

@Composable
fun HikeCardMapPreview(feature: Feature) {
    val coordinates = mutableListOf<Point>()
    feature.geometry.coordinates.forEach { coordList ->
        coordList.forEach { coord ->
            coordinates.add(Point.fromLngLat(coord[1], coord[0]))
        }
    }

    val bbox = getBoundingBox(coordinates)
    val center = Point.fromLngLat(
        (bbox.west() + bbox.east()) / 2,
        (bbox.north() + bbox.south()) / 2
    )
    val mapState = rememberMapState {
        gesturesSettings = GesturesSettings {
            rotateEnabled = false
            scrollEnabled = false
            pitchEnabled = false
            doubleTapToZoomInEnabled = false
            quickZoomEnabled = false
            pinchToZoomEnabled = false
        }
    }
    val mapViewPortState = rememberMapViewportState {
        setCameraOptions {
            center(center)
            zoom(calculateIdealZoom(bbox))
            pitch(0.0)
            bearing(0.0)
        }
    }
    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clipToBounds()
    ) {
        MapboxMap(
            mapViewportState = mapViewPortState,
            mapState = mapState,
            style = { MapboxStandardStyle() },
            scaleBar = {},
            logo = {},
            attribution = {},
        ) {
            val sourceId = remember { "source-${feature.hashCode()}" }
            val layerId = remember { "layer-${feature.hashCode()}" }
            MapEffect(Unit) { mapView ->
                val mapboxMap = mapView.mapboxMap
                mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    style.removeStyleLayer(layerId)
                    style.removeStyleSource(sourceId)

                    val lineStrings = feature.geometry.coordinates.map { coords ->
                        val points = coords.map { Point.fromLngLat(it[1], it[0]) }
                        com.mapbox.geojson.LineString.fromLngLats(points)
                    }

                    val multiLineString = com.mapbox.geojson.MultiLineString.fromLineStrings(lineStrings)
                    val geoJsonFeature = com.mapbox.geojson.Feature.fromGeometry(multiLineString)

                    val featureCollection = com.mapbox.geojson.FeatureCollection.fromFeatures(listOf(geoJsonFeature))

                    val source = GeoJsonSource.Builder(sourceId)
                        .data(featureCollection.toJson())
                        .build()
                    style.addSource(source)

                    val lineLayer = LineLayer(layerId, sourceId)
                    lineLayer.lineColor("rgba(100, 100, 100, 1)")
                    lineLayer.lineWidth(3.0)
                    lineLayer.lineOpacity(0.8)
                    style.addLayer(lineLayer)
                }
            }
        }
    }
}

private fun calculateIdealZoom(bbox: BoundingBox): Double {
    val latDiff = bbox.north() - bbox.south()
    val lngDiff = bbox.east() - bbox.west()
    val maxDiff = maxOf(latDiff, lngDiff)

    //endre disse verdiene etter testing?
    return when {
        maxDiff > 5.0 -> 1.0
        maxDiff > 1.0 -> 4.0
        maxDiff > 0.5 -> 6.0
        maxDiff > 0.1 -> 8.0
        maxDiff > 0.05 -> 7.0
        else -> 12.0
    }
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

    return BoundingBox.fromLngLats(minLng, minLat, maxLng, maxLat)
}