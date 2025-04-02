package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroupState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapViewer(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    val focusManager = LocalFocusManager.current
    val polylineAnnotationGroupState = PolylineAnnotationGroupState()

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(mapboxUIState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Update viewport and fetch hikes when pointer coordinates change
    LaunchedEffect(mapboxUIState.pointerCoordinates) {
        mapViewportState.easeTo(
            cameraOptions {
                zoom(12.0)
                center(mapboxUIState.pointerCoordinates)
                pitch(0.0)
                bearing(0.0)
            }
        )
        mapboxViewModel.setLoaderState(isLoading = true)
        homeScreenViewModel.fetchHikes(
            mapboxUIState.pointerCoordinates.latitude(),
            mapboxUIState.pointerCoordinates.longitude(),
            5,
            "Fotrute",
            500
        )
        homeScreenViewModel.fetchForecast(
            mapboxUIState.pointerCoordinates.latitude(),
            mapboxUIState.pointerCoordinates.longitude()
        )
        homeScreenViewModel.fetchAlerts()
    }

    LaunchedEffect(homeScreenUIState.hikes) {
        mapboxViewModel.updatePolylineAnnotationsFromFeatures(homeScreenUIState.hikes)
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
        PolylineAnnotationGroup(
            mapboxUIState.polylineAnnotations,
            polylineAnnotationGroupState = polylineAnnotationGroupState
        )

        val marker = rememberIconImage(R.drawable.red_marker)
        PointAnnotation(point = mapboxUIState.pointerCoordinates) {
            iconImage = marker
        }
    }
}
