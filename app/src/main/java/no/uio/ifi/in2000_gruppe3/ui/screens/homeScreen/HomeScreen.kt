package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.BottomSheetDrawer
import no.uio.ifi.in2000_gruppe3.ui.loaders.MapLoader
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SearchBarForMap
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SuggestionColumn
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapStyleDropdownMenu
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    favoritesViewModel: FavoritesViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    val locationPermissionRequest = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted - start location updates
                mapboxViewModel.startLocationUpdates()
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted
                mapboxViewModel.startLocationUpdates()
            }

            else -> {
                // No location access granted - use default location
            }
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
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
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

            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        3.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 25.dp)
                ) {
                    ForecastDisplay(
                        homeScreenViewModel = homeScreenViewModel,
                        mapboxViewModel = mapboxViewModel,
                        visableOnMap = true,
                        showTemperature = true,
                        modifier = Modifier.weight(0.35f)
                    )

                    SearchBarForMap(
                        mapboxViewModel = mapboxViewModel,
                        homeScreenViewModel = homeScreenViewModel,
                        modifier = Modifier.weight(1.5f)
                    )

                    MapStyleDropdownMenu(
                        mapboxViewModel = mapboxViewModel,
                        modifier = Modifier.weight(0.25f)
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
                navController = navController
            )
        }
    }
}