package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.coroutineScope
import no.uio.ifi.in2000_gruppe3.ui.ai.AanundFigure
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.BottomSheetDrawer
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent
import no.uio.ifi.in2000_gruppe3.ui.loaders.MapLoader
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SearchBarForMap
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SuggestionColumn
import no.uio.ifi.in2000_gruppe3.ui.mapbox.AlertsDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapStyleSelector
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxZoomButtons
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.mapbox.ResetMapCenterButton
import no.uio.ifi.in2000_gruppe3.ui.networkSnackbar.NetworkSnackbar
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    favoritesViewModel: FavoritesViewModel,
    mapboxViewModel: MapboxViewModel,
    openAIViewModel: OpenAIViewModel,
    navController: NavHostController
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    val targetSheetState by homeScreenViewModel.sheetStateTarget.collectAsState()
    val isControlsVisible = targetSheetState == SheetDrawerDetent.HIDDEN ||
                            targetSheetState == SheetDrawerDetent.SEMIPEEK

    // Calculate vertical offset based on sheet state
    val sheetOffset = remember(targetSheetState) {
        when (targetSheetState) {
            SheetDrawerDetent.SEMIPEEK -> (-200).dp
            else -> 0.dp
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            NetworkSnackbar(snackbarHostState, coroutineScope)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            MapViewer(
                homeScreenViewModel = homeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
                favoritesViewModel = favoritesViewModel,
            )

            MapLoader(
                mapboxViewModel = mapboxViewModel
            )

            Column(
                modifier = Modifier.padding(top = 90.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    color = Color.Transparent
                ) {
                    ForecastDisplay(
                        homeScreenViewModel = homeScreenViewModel,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    color = Color.Transparent
                ) {
                    AlertsDisplay(
                        homeScreenViewModel = homeScreenViewModel,
                        mapboxViewModel = mapboxViewModel
                    )
                }
            }

            if (isControlsVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 10.dp)
                        .offset(y = sheetOffset)
                ) {
                    AanundFigure(
                        homeScreenViewModel = homeScreenViewModel,
                        mapBoxViewModel = mapboxViewModel,
                        navController = navController
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 36.dp, end = 8.dp)
                        .offset(y = sheetOffset)
                ) {
                    ResetMapCenterButton(
                        homeScreenViewModel = homeScreenViewModel,
                        mapboxViewModel = mapboxViewModel
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    MapStyleSelector(
                        mapboxViewModel = mapboxViewModel
                    )
                }

//                Box(
//                    modifier = Modifier
//                        .align(Alignment.TopEnd)
//                        .padding(top = 90.dp, end = 8.dp)
//
//                ) {
//                    MapboxZoomButtons(
//                        mapboxViewModel = mapboxViewModel
//                    )
//                }
            }

            Column {
                Row(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 25.dp)
                ) {
                    SearchBarForMap(
                        mapboxViewModel = mapboxViewModel,
                        homeScreenViewModel = homeScreenViewModel
                    )
                }

                SuggestionColumn(
                    mapboxViewModel = mapboxViewModel
                )
            }

            BottomSheetDrawer(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeViewModel,
                mapboxViewModel = mapboxViewModel,
                openAIViewModel = openAIViewModel,
                navController = navController
            )
        }
    }
}
