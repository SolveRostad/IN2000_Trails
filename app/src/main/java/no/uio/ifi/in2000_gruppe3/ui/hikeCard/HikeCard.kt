package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.jeziellago.compose.markdowntext.MarkdownText
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen.GeminiViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HikeCard(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    favoritesViewModel: FavoritesViewModel,
    mapboxViewModel: MapboxViewModel,
    geminiViewModel: GeminiViewModel,
    navController: NavHostController
) {
    val homeUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()
    val geminiUIState by geminiViewModel.geminiUIState.collectAsState()

    val checkedState = remember {
        mutableStateOf(favoritesViewModel.isHikeFavorite(hikeUIState.feature))
    }

    val difficulty = getDifficultyInfo(hikeUIState.feature.properties.gradering)

    hikeScreenViewModel.getHikeDescription(
        homeScreenViewModel = homeScreenViewModel,
        geminiViewModel = geminiViewModel
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        LazyColumn {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    HikeCardMapPreview(mapboxViewModel, hikeUIState.feature)

                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                    ) {
                        ForecastDisplay(
                            homeScreenViewModel,
                            mapboxViewModel,
                            showTemperature = false
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Rute detaljer",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                InfoItem(
                                    icon = ImageVector.vectorResource(R.drawable.terrain_icon),
                                    label = "Type",
                                    value = difficulty.label,
                                    iconTint = MaterialTheme.colorScheme.primary
                                )
                                InfoItem(
                                    icon = ImageVector.vectorResource(R.drawable.mountain),
                                    label = "Vanskelighet",
                                    value = difficulty.label,
                                    iconTint = difficulty.color
                                )
                                InfoItem(
                                    icon = ImageVector.vectorResource(id = R.drawable.distance_icon),
                                    label = "Lengde",
                                    value = String.format(
                                        Locale("nb", "NO"),
                                        "%.2f km",
                                        hikeUIState.feature.properties.distance_meters.toFloat() / 1000.0
                                    ),
                                    iconTint = Color(0xFF4CAF50)
                                )
                                InfoItem(
                                    icon = ImageVector.vectorResource(id = R.drawable.windmill),
                                    label = "Vindhastighet",
                                    value = "${homeUIState.forecast?.properties?.timeseries?.firstOrNull()?.data?.instant?.details?.wind_speed} m/s",
                                    iconTint = Color(0xFF2196F3)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Rute beskrivelse",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MarkdownText(
                        markdown = geminiUIState.response,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57B9FF)),
                        onClick = { navController.navigate("locationForecast") }
                    ) {
                        Text(text = "Se været andre dager")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

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
}