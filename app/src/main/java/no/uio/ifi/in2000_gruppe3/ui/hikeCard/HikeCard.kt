package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import dev.jeziellago.compose.markdowntext.MarkdownText
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.data.date.calculateDaysAhead
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.loaders.Loader
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ForecastDisplay
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.LocationForecastSmallCard
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import java.time.LocalDate

@Composable
fun HikeCard(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    favoritesViewModel: FavoritesScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    openAIViewModel: OpenAIViewModel,
    navController: NavHostController,
    checkedState: MutableState<Boolean>
) {
    val homeUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()
    val openAIUIState by openAIViewModel.openAIUIState.collectAsState()

    val todaysDay = getTodaysDay()

    // Shows current temperature and wind speed on launch, then shows average based on selected day
    var displayTimeSeries = homeUIState.forecast?.properties?.timeseries?.firstOrNull()
    var averageTemperature by remember { mutableStateOf(displayTimeSeries?.data?.instant?.details?.air_temperature) }
    var averageWindSpeed by remember { mutableStateOf(displayTimeSeries?.data?.instant?.details?.wind_speed) }

    LaunchedEffect(hikeUIState.selectedDay) {
        val daysAhead = calculateDaysAhead(todaysDay, hikeUIState.selectedDay)
        hikeScreenViewModel.updateSelectedDate(
            LocalDate.now().plusDays(daysAhead.toLong()).toString()
        )

        displayTimeSeries =
            homeScreenViewModel.timeSeriesFromDate(hikeUIState.selectedDate)?.firstOrNull()

        averageTemperature = homeScreenViewModel.daysAverageTemp(hikeUIState.selectedDate)
        averageWindSpeed = homeScreenViewModel.daysAverageWindSpeed(hikeUIState.selectedDate)

        if (hikeScreenViewModel.needsDescriptionLoading(hikeUIState.selectedDay)) {
            hikeScreenViewModel.getHikeDescription(
                homeScreenViewModel = homeScreenViewModel,
                openAIViewModel = openAIViewModel,
                selectedDay = hikeUIState.selectedDay,
                selectedDate = hikeUIState.selectedDate
            )
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f)
        ) {
            item {
                Box {
                    HikeCardMapPreview(mapboxViewModel, hikeUIState.feature)

                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .align(Alignment.TopEnd)
                            .background(
                                color = Color.White.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        color = Color.Transparent
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                showTemperature = false,
                                date = hikeUIState.selectedDate
                            )
                            Text(
                                text = averageTemperature?.let { "%.1f°C".format(it) } ?: "N/A",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rute detaljer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )

                    WeekdaySelector(
                        hikeScreenViewModel = hikeScreenViewModel
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoItem(
                        icon = ImageVector.vectorResource(R.drawable.mountain),
                        label = "Type",
                        value = "Gåtur", //hikeUIState.feature.properties.type
                        iconTint = MaterialTheme.colorScheme.primary
                    )
                    InfoItem(
                        icon = ImageVector.vectorResource(R.drawable.terrain_icon),
                        label = "Vanskelighet",
                        value = hikeUIState.feature.difficultyInfo.label,
                        iconTint = hikeUIState.feature.difficultyInfo.color
                    )
                    InfoItem(
                        icon = ImageVector.vectorResource(id = R.drawable.distance_icon),
                        label = "Lengde",
                        value = (hikeUIState.feature.properties.distance_meters.toFloat() / 1000.0).let {
                            "%.2f km".format(
                                it
                            )
                        },
                        iconTint = Color(0xFF4CAF50)
                    )
                    InfoItem(
                        icon = ImageVector.vectorResource(id = R.drawable.wind),
                        label = "Vindhastighet",
                        value = averageWindSpeed?.let { "%.1f m/s".format(it) } ?: "N/A",
                        iconTint = Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    MarkdownText(
                        markdown = openAIUIState.response,
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 22.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    if (openAIUIState.isLoading || openAIUIState.isStreaming) {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Loader()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LocationForecastSmallCard(
                    day = hikeUIState.selectedDay,
                    date = hikeUIState.selectedDate,
                    homeScreenViewModel = homeScreenViewModel,
                    hikeScreenViewModel = hikeScreenViewModel,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57B9FF)),
                    onClick = { navController.navigate(Screen.LocationForecast.route) }
                ) {
                    Text(text = "Se været andre dager")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            checkedState.value = !checkedState.value
                            if (checkedState.value) {
                                favoritesViewModel.addFavorite(hikeUIState.feature.properties.fid)
                            } else {
                                favoritesViewModel.deleteFavorite(hikeUIState.feature.properties.fid)
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (checkedState.value) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = if (checkedState.value) Color.Red else Color.Gray,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = if (checkedState.value) "Fjern fra favoritter" else "Legg til i favoritter")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
