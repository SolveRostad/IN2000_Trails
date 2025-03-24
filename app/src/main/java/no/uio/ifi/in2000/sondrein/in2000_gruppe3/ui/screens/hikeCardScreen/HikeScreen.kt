package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.hikeCard.HikeCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HikeScreen(
    favoritesViewModel: FavoritesViewModel,
    hikeScreenviewModel: HikeScreenViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavHostController
) {
    val uiState by hikeScreenviewModel.hikeScreenUIState.collectAsState()

    val checkedState = remember {
        mutableStateOf(favoritesViewModel.isHikeFavorite(uiState.feature))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.feature.properties.rutenavn) }, //Må kanskje endres
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            HikeCard(hikeScreenviewModel, homeScreenViewModel)

            IconToggleButton(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if (it) {
                        favoritesViewModel.addHike(uiState.feature)
                    } else {
                        favoritesViewModel.deleteHike(uiState.feature)
                    }
                }
            ) {
                val tint by animateColorAsState(if (checkedState.value) Color.Red else Color.Gray)
                Icon(
                    if (checkedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Toggle favorite",
                    tint = tint
                )
            }

        }
    }
}