package no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.hikeCard.HikeCard
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HikeScreen(
    homeScreenViewModel: HomeScreenViewModel,
    favoritesViewModel: FavoritesViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    openAIViewModel: OpenAIViewModel,
    navController: NavHostController
) {
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()

    val checkedState = remember {
        mutableStateOf(favoritesViewModel.isHikeFavorite(hikeUIState.feature))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = hikeUIState.feature.properties.desc ?: "Ukjent rutenavn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        checkedState.value = !checkedState.value
                        if (checkedState.value) {
                            favoritesViewModel.addHike(hikeUIState.feature)
                        } else {
                            favoritesViewModel.deleteHike(hikeUIState.feature)
                        }
                    }) {
                        Icon(
                            imageVector = if (checkedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = if (checkedState.value) Color.Red else Color.Gray
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HikeCard(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                favoritesViewModel = favoritesViewModel,
                mapboxViewModel = mapboxViewModel,
                openAIViewModel = openAIViewModel,
                navController = navController,
                checkedState = checkedState
            )
        }
    }
}
