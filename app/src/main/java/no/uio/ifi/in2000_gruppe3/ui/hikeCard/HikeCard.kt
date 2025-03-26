package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun HikeCard(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenviewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    favoritesViewModel: FavoritesViewModel,
    navcontroller: NavHostController
) {
    val hikeUIState by hikeScreenviewModel.hikeScreenUIState.collectAsState()
    val checkedState = remember {
        mutableStateOf(favoritesViewModel.isHikeFavorite(hikeUIState.feature))
    }

    Card {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                Text(
                    text = hikeUIState.feature.properties.rutenavn,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                HikeCardMapPreview(mapboxViewModel, hikeUIState.feature)

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Rute detaljer",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Avstand til turen: ${hikeUIState.feature.properties.distance_meters.toFloat() / 1000.0} km")
                        Text(text = if (hikeUIState.feature.properties.gradering.isEmpty()) "Vanskelighetsgrad: Ukjent" else "Vanskelighetsgrad: ${hikeUIState.feature.properties.gradering.first()}")
                        Text(text = "Forhold: ")
                        Text(text = "Type: ")
                    }

                    ForecastDisplay(
                        homeScreenViewModel,
                        mapboxViewModel
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rute beskrivelse",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "This is a detailed description of the hiking route. It includes information about the terrain, landmarks, and what to expect along the trail."
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = { navcontroller.navigate("locationForecast") }
                ) {
                    Text(text = "Se flere dager")
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconToggleButton(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = it
                            if (it) {
                                favoritesViewModel.addHike(hikeUIState.feature)
                            } else {
                                favoritesViewModel.deleteHike(hikeUIState.feature)
                            }
                        }
                    ) {
                        val tint by animateColorAsState(if (checkedState.value) Color.Red else Color.Gray)
                        Icon(
                            if (checkedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Toggle favorite",
                            tint = tint,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Text(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        text = if (checkedState.value) "Fjern fra favoritter" else "Legg til i favoritter"
                    )
                }
            }
        }
    }
}