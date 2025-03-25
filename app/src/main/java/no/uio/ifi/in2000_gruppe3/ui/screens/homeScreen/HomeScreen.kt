package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomSheetDrawer
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    // Oppdaterer ViewportState når pekerposisjonen endres
    LaunchedEffect(mapboxUIState.pointerCoordinates) {
        homeScreenViewModel.fetchForecast(
            mapboxUIState.pointerCoordinates.latitude(),
            mapboxUIState.pointerCoordinates.longitude()
        )
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            // BottomSheetDrawer for å vise turer
            BottomSheetDrawer(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeViewModel,
                mapboxViewModel = mapboxViewModel,
                navController = navController
            ) {
                MapViewer(
                    homeScreenViewModel,
                    mapboxViewModel
                )
            }
        }
    }
}