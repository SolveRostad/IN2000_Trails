package no.uio.ifi.in2000_gruppe3.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.hikeCard.SmallHikeCard
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@Composable
fun HikesDone(
    hikeScreenViewModel: HikeScreenViewModel,
    favoritesViewModel: FavoritesViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
//    val favoriteUIState by favoritesViewModel.favoritesScreenUIState.collectAsState()
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize(),
//        contentPadding = PaddingValues(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        item { Text(text = "Turer du har gått") }
//        items(favoriteUIState.hikesDone) { feature ->
//            SmallHikeCard(
//                mapboxViewModel = mapboxViewModel,
//                feature = feature,
//                onClick = {
//                    hikeScreenViewModel.updateHike(feature)
//                    navController.navigate(Screen.HikeScreen.route)
//                }
//            )
//            HikesDoneNotesField()
//        }
//    }
}
