package no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.core.BottomSheet
import com.composables.core.DragIndication
import com.composables.core.rememberBottomSheetState
import no.uio.ifi.in2000_gruppe3.ui.hikeCard.SmallHikeCard
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDrawer(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()

    val targetSheetState by homeScreenViewModel.sheetStateTarget.collectAsState()
    val detents = SheetDrawerDetent.entries.map { it.value }

    val sheetState = rememberBottomSheetState(
        initialDetent = targetSheetState.value,
        detents = detents
    )

    val alpha by animateFloatAsState(targetValue = sheetState.offset)

    LaunchedEffect(mapboxUIState.searchResponse, mapboxUIState.searchQuery) {
        if (mapboxUIState.searchResponse.isNotEmpty() && mapboxUIState.searchQuery.isNotEmpty()) {
            sheetState.animateTo(SheetDrawerDetent.HIDDEN.value)
        } else {
            sheetState.animateTo(targetSheetState.value)
        }
    }

    LaunchedEffect(targetSheetState) {
        sheetState.animateTo(targetSheetState.value)
    }

    LaunchedEffect(sheetState.currentDetent) {
        homeScreenViewModel.setSheetState(
            SheetDrawerDetent.entries.find { it.value.identifier == sheetState.targetDetent.identifier }
                ?: SheetDrawerDetent.SEMIPEEK
        )
    }

    BottomSheet(
        state = sheetState,
        modifier = Modifier
            // Only clip to RoundedCornerShape if not fully expanded
            .then(
                if (sheetState.currentDetent.identifier != SheetDrawerDetent.FULLYEXPAND.value.identifier) {
                    Modifier.clip(RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            )
            .fillMaxWidth()
            .alpha(alpha)
            .background(Color.White)
            .height(1200.dp)
    ) {
        Column {
            DragIndication(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                    .width(42.dp)
                    .height(4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            LazyColumn(
                modifier = Modifier.clipToBounds(),
                contentPadding = PaddingValues(16.dp)
            ) {
                if (homeScreenUIState.hikes.isEmpty()) {
                    item {
                        EmptyDrawerInfoCard(
                            navController = navController
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "De fem nærmeste rutene",
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(homeScreenUIState.hikes) { feature ->
                        SmallHikeCard(
                            mapboxViewModel = mapboxViewModel,
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
