package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.BottomSheetDrawer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.homeScreenUIState.collectAsState()

    //henter værdata basert på koordinatene
    LaunchedEffect(uiState.pointerCoordinates) {
        viewModel.fetchForecast(
            uiState.pointerCoordinates.latitude(),
            uiState.pointerCoordinates.longitude()
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
            // Bottom sheet state for å kunne åpne og lukke bottom sheet
            val bottomSheetState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded
                )
            )

            // BottomSheetDrawer for å vise turer
            BottomSheetDrawer(
                bottomSheetState = bottomSheetState,
                viewModel = viewModel,
                hikeViewModel = hikeViewModel,
                navController = navController
            ) {
                MapViewer(viewModel = viewModel)
            }
        }
    }
}