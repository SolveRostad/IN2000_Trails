package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.homeScreenUIState.collectAsState()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = "Hiking Routes",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                MapViewer(viewModel)
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(uiState.turer.features) { feature ->
                    SmallHikeCard(
                        feature,
                        onClick = {
                            hikeViewModel.updateHike(feature)
                            navController.navigate(Screen.HikeScreen.route)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}