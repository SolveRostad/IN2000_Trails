package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home

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
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.Hike
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.SmallHikeCard

@Composable
fun HomeScreen(
    onHikeClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    viewModel: HomeScreenViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.homeScreenUIState.collectAsState()
    Scaffold(
        bottomBar = {
            BottomBar(
                onHomeClick = { /* Already on home */ },
                onFavoritesClick = onFavoritesClick,
                navController = navController
            )
        }
    ) { paddingValues ->

        // Dummydata
        val hikes = listOf(
            Hike(1, "Mountain Trail", 8.5, "Medium", ""),
            Hike(2, "Forest Walk", 5.2, "Easy", ""),
            Hike(3, "River Path", 10.0, "Hard", "")
        )

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
                items(hikes) { hike ->
                    SmallHikeCard(hike = hike, onClick = { onHikeClick(hike.id) })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}