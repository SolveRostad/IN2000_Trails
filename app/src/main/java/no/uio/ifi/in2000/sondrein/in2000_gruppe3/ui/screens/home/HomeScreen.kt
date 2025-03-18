package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.Hike
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.HikeCard

@Composable
fun HomeScreen(
    onHikeClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /* Already on home */ }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites"
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        // Dummydata
        val hikes = listOf(
            Hike(1, "Mountain Trail", 8.5, "Medium", ""),
            Hike(2, "Forest Walk", 5.2, "Easy", ""),
            Hike(3, "River Path", 10.0, "Hard", "")
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Hiking Routes",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            item {
                MapViewer()
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(hikes) { hike ->
                HikeCard(hike = hike, onClick = { onHikeClick(hike.id) })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}