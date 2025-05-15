package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroup
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotationGroupState
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.profile.activities.ActivityScreenViewModel

@Composable
fun MapViewer(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    favoritesViewModel: FavoritesScreenViewModel,
    activityScreenViewModel: ActivityScreenViewModel
) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val focusManager = LocalFocusManager.current
    val polylineAnnotationGroupState = PolylineAnnotationGroupState()
    val mapViewportState = mapboxViewModel.mapViewportState
    val firstInit = remember { mutableStateOf(true) }

    DisposableEffect(Unit) {
        mapboxViewModel.centerOnUserPosition(animationDuration = 0)
        onDispose { }
    }

    LaunchedEffect(homeScreenUIState.hikes) {
        mapboxViewModel.updatePolylineAnnotationsFromFeatures(homeScreenUIState.hikes)
    }

    LaunchedEffect(mapboxUIState.pointerCoordinates) {
        if (mapboxUIState.pointerCoordinates != null) {
            homeScreenViewModel.fetchForecast(mapboxUIState.pointerCoordinates!!)
            homeScreenViewModel.fetchAlerts()
            hikeScreenViewModel.updateSelectedDay(getTodaysDay())
        }
    }

    LaunchedEffect(mapboxUIState.centerOnUserTrigger) {
        homeScreenViewModel.fetchAlerts()
        mapboxUIState.latestUserPosition?.let {
            homeScreenViewModel.fetchForecast(it)
        }
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        style = { MapStyle(mapboxUIState.mapStyle) },
        onMapClickListener = { point ->
            mapboxViewModel.setLoaderState(true)
            mapboxViewModel.centerOnPoint(point)
            homeScreenViewModel.fetchHikes(
                point.latitude(),
                point.longitude(),
                5,
                "Fotrute",
                500
            )
            focusManager.clearFocus()
            mapboxViewModel.updatePointerCoordinates(point)
            true
        },
        compass = {
            Compass(
                modifier = Modifier.padding(top = 80.dp, end = 8.dp)
            )
        },
        logo = {},
        scaleBar = {},
        attribution = {}
    ) {
        PolylineAnnotationGroup(
            mapboxUIState.polylineAnnotations,
            polylineAnnotationGroupState = polylineAnnotationGroupState
        )

        val marker = rememberIconImage(R.drawable.red_marker)
        if (mapboxUIState.pointerCoordinates != null) {
            PointAnnotation(point = mapboxUIState.pointerCoordinates!!) {
                iconImage = marker
            }
        }

        MapEffect(Unit) { mapView ->
            mapView.location.updateSettings {
                locationPuck = createDefault2DPuck(withBearing = true)
                enabled = true
            }

            mapView.location.addOnIndicatorPositionChangedListener { point ->
                favoritesViewModel.updateUserLocationFromMapbox()
                activityScreenViewModel.updateProfileLocationFromMapbox()
                mapboxViewModel.updateLatestUserPosition(point)
                if (firstInit.value) {
                    homeScreenViewModel.fetchForecast(point)
                    firstInit.value = false
                }
            }
            mapView.mapboxMap.subscribeCameraChanged {
                if (homeScreenViewModel.sheetStateTarget.value.value.identifier != "hidden") {
                    homeScreenViewModel.setSheetState(SheetDrawerDetent.SEMIPEEK)
                }
            }
        }
    }
}
