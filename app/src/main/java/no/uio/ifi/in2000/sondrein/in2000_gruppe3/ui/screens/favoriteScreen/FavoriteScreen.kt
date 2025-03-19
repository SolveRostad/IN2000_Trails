package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.Hike

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onHikeClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    navController: NavHostController
) {
    // Dummydata
    val favoriteHikes = remember {
        listOf(
            Hike(2, "Forest Walk", 5.2, "Easy", ""),
            Hike(3, "River Path", 10.0, "Hard", "")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Favorite Hikes") }
            )
        },
        bottomBar = {
            BottomBar(
                onHomeClick = onHomeClick,
                onFavoritesClick = { /* Already on favorites */ },
                navController = navController
            )
        }
    ) { paddingValues ->
        if (favoriteHikes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Text(
                    text = "You don't have any favorite hikes yet.",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
//                items(ui) { hike ->
//                    SmallHikeCard(hike = hike, onClick = { onHikeClick(hike.id) })
//                }
            }
        }
    }
}