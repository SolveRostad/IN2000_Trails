package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
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
            5,
            "Fotrute",
            500
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
        attribution = {},
        style = {
            when (mapboxUIState.mapStyle) {
                MapStyles.OUTDOORS -> MapStyle(style = Style.OUTDOORS)
                MapStyles.STANDARD_SATELLITE -> MapStyle(style = Style.STANDARD_SATELLITE)
            }
        },
    ) {
        // Adds hikes to map
        Log.d("BBBBBBB", "Hikes: ${homeScreenUIState.hikes}")
        homeScreenUIState.hikes.forEach { feature ->
            val points = feature.geometry.coordinates.map { coordinate ->
                Point.fromLngLat(coordinate[0], coordinate[1])
            }
            Log.d("AAAA", "Points: $points")
            PolylineAnnotation(
                points = points
            ) {
                lineColor = feature.color
                lineWidth = 5.0
                lineOpacity = 0.7
            }
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
        ForecastDisplay(
            homeScreenViewModel,
            mapboxViewModel
        )

        // Søkefelt for å søke etter steder
        SearchBarForMap(
            mapboxViewModel,
            modifier = Modifier.padding(top = 11.dp)
        )

        // Dropdown menu for å velge kartstil
        MapStyleDropdownMenu(mapboxViewModel)
    }
}
