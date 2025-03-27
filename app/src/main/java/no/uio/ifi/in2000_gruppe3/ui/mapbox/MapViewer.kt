package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import com.mapbox.geojson.Point
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.MapboxMapComposable
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroupState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

/**
 * MapViewer er en composable som viser et kart med mulighet for å velge kartstil og lysmåte
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapViewer(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val focusManager = LocalFocusManager.current

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(mapboxUIState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Update viewport and fetch hikes when pointer coordinates change
    LaunchedEffect(mapboxUIState.pointerCoordinates, mapboxUIState.mapStyle) {
        mapViewportState.easeTo(
            cameraOptions {
                zoom(12.0)
                center(mapboxUIState.pointerCoordinates)
                pitch(0.0)
                bearing(0.0)
            }
        )
        homeScreenViewModel.fetchHikes(
            mapboxUIState.pointerCoordinates.latitude(),
            mapboxUIState.pointerCoordinates.longitude(),
            5,
            "Fotrute",
            500
        )
    }
    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        onMapClickListener = { point ->
            focusManager.clearFocus()
            mapboxViewModel.updatePointerCoordinates(point)
            true
        },
        scaleBar = {},
        logo = {},
        attribution = {},
        style = { MapStyle(mapboxUIState.mapStyle) }
    ) {
        PolyLines(homeScreenUIState.hikes, mapboxViewModel)

        val marker = rememberIconImage(R.drawable.red_marker)
        PointAnnotation(point = mapboxUIState.pointerCoordinates) {
            iconImage = marker
        }
    }
}

@Composable
@MapboxMapComposable
fun PolyLines(features: List<Feature>, mapboxViewModel: MapboxViewModel) {
    mapboxViewModel.setLoaderState(true)
    val polylineAnnotationGroupState = remember { PolylineAnnotationGroupState() }

    PolylineAnnotationGroup(
        polylineAnnotationGroupState = polylineAnnotationGroupState,
        annotations = mutableListOf<PolylineAnnotationOptions>().apply {
            features.forEach { feature ->
                add(
                    PolylineAnnotationOptions()
                        .withPoints(
                            feature.geometry.coordinates.map { coordinate ->
                                Point.fromLngLat(coordinate[0], coordinate[1])
                            }
                        )
                        .withLineColor(feature.color!!.toArgb())
                        .withLineWidth(7.0)
                        .withLineOpacity(0.7)
                        .withLineBorderColor(Color.White.toArgb())
                        .withLineBorderWidth(2.0)
                )
            }
        }
    )
}