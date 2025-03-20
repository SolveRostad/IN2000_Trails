package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

/**
 * MapViewer er en composable som viser et kart med mulighet for å velge kartstil og lysmåte
 */
@Composable
fun MapViewer(viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUIState.collectAsState()

    // Observe the map style and dark mode from ViewModel to trigger recomposition
    val mapStyle by remember { derivedStateOf { viewModel.mapStyle } }
    val isDarkMode by remember { derivedStateOf { viewModel.mapIsDarkmode } }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(uiState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Oppdaterer ViewportState når pekerposisjonen endres
    LaunchedEffect(uiState.pointerCoordinates) {
        mapViewportState.setCameraOptions {
            zoom(12.0)
            center(uiState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    Box {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = {
                when (mapStyle) {
                    "STANDARD" -> {
                        MapboxStandardStyle {
                            lightPreset =
                                if (isDarkMode) LightPresetValue.NIGHT else LightPresetValue.DAY
                        }
                    }
                    "SATELLITE" -> {
                        MapStyle(style = Style.STANDARD_SATELLITE)
                    }
                    "OUTDOORS" -> {
                        MapStyle(style = Style.OUTDOORS)
                    }
                }
            },
            onMapClickListener = { point ->
                viewModel.updatePointerCoordinates(point)
                viewModel.fetchTurer(point.latitude(), point.longitude(), 5)
                true
            },
            // Fjerner skala, logo og attribusjon fra kartet
            scaleBar = {},
            logo = {},
            attribution = {}
        ) {
            // Legger til markør for pekerposisjonen
            val marker = rememberIconImage(R.drawable.red_marker)
            PointAnnotation(point = uiState.pointerCoordinates) {
                iconImage = marker
            }

            // Legger til ruter på kartet
            uiState.turer.features.forEach { feature ->
                val color = viewModel.getViableRouteColor()
                feature.geometry.coordinates.forEach { coordinates ->
                    val points = mutableListOf<Point>()
                    coordinates.forEach {
                        points.add(Point.fromLngLat(it[1], it[0]))
                    }
                    PolylineAnnotation(
                        points = points
                    ) {
                        lineColor = color
                        lineWidth = 6.0
                        lineOpacity = 0.8
                    }
                }
            }
        }

        // Legger til temperatur på kartet
        ForecastDisplay(viewModel)

        // Dropdown menu for å velge kartstil
        MapStyleDropdownMenu(viewModel)

        // Søkefelt for å søke etter steder
        SearchBarForMap(viewModel)
    }
}
