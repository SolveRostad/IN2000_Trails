package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.loaders.MapLoader
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SearchBarForMap
import no.uio.ifi.in2000_gruppe3.ui.mapSearchbar.SuggestionColumn
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapStyleDropdownMenu
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomSheetDrawer
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
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
                MapLoader(
                    mapboxViewModel
                )
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                    ) {
                        ForecastDisplay(
                            homeScreenViewModel,
                            mapboxViewModel,
                            visableOnMap = true,
                            modifier = Modifier.weight(0.35f)
                        )
                        SearchBarForMap(
                            mapboxViewModel,
                            modifier = Modifier.weight(1.5f)
                        )
                        MapStyleDropdownMenu(
                            mapboxViewModel,
                            modifier = Modifier.weight(0.25f)
                        )
                    }
                    SuggestionColumn(
                        mapboxViewModel
                    )
                }
            }
        }
    }
}