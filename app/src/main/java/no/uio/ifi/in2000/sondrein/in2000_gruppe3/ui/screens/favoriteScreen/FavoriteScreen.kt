package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.hikeCard.SmallHikeCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    navController: NavHostController
) {
    val favoriteUIState by favoritesViewModel.favoritesScreenUIState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Favorite Hikes") }) },
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        when {
            favoriteUIState.favorites.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Her var det tomt gitt 🤔",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(favoriteUIState.favorites) { feature ->
                        SmallHikeCard (
                            feature = feature,
                            onClick = {
                                hikeScreenViewModel.updateHike(feature)
                                navController.navigate(Screen.HikeScreen.route)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}