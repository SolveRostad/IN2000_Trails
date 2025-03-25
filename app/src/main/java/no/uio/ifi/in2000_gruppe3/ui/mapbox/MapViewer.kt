package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

/**
 * MapViewer er en composable som viser et kart med mulighet for å velge kartstil og lysmåte
 */
@Composable
fun MapViewer(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
) {

    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(mapboxUIState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Update viewport and fetch turer when pointer coordinates change
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
            5
        )
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,

        onMapClickListener = { point ->
            mapboxViewModel.updatePointerCoordinates(point)
            true
        },
        scaleBar = {},
        logo = {},
        attribution = {}
    ) {
        // Adds hikes to map
        MapEffect(homeScreenUIState.hikes) { mapView ->
            val mapboxMap = mapView.mapboxMap
            val style = mapboxMap.style

            mapboxViewModel.updateLineStringsFromFeatures(homeScreenUIState.hikes.features, style)
        }

        // Adds marker for pointer location
        val marker = rememberIconImage(R.drawable.red_marker)
        PointAnnotation(point = mapboxUIState.pointerCoordinates) {
            iconImage = marker
        }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        // Legger til temperatur på kartet
        ForecastDisplay(homeScreenViewModel, modifier = Modifier.padding(end = 8.dp))

        // Søkefelt for å søke etter steder
        SearchBarForMap(mapboxViewModel, modifier = Modifier)

        // Dropdown menu for å velge kartstil
        MapStyleDropdownMenu(
            homeScreenViewModel,
            mapboxViewModel,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
