package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.data.date.getCurrentTime
import no.uio.ifi.in2000_gruppe3.data.date.getDateFormatted
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDate
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenUIState
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationForecastSmallCard(
    day: String,
    date: String,
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    val homeUIState by homeScreenViewModel.homeScreenUIState.collectAsState()

    val todaysDay = getTodaysDay()
    val todaysDate = getTodaysDate()
    val dateFormatted = getDateFormatted(date)
    val currentTime = getCurrentTime()

    val daysHighestTemp = daysHighestTemp(homeUIState, date)
    val daysLowestTemp = daysLowestTemp(homeUIState, date)

    val visibleBoxesCount = listOf(
        date > todaysDate || (date == todaysDate && currentTime < "06:00:00"),
        date > todaysDate || (date == todaysDate && currentTime < "12:00:00"),
        date > todaysDate || (date == todaysDate && currentTime < "18:00:00"),
        date > todaysDate || (date == todaysDate && currentTime < "23:59:00")
    ).count { it }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        onClick = {
            hikeScreenViewModel.updateDate(day, date, dateFormatted)
            navController.navigate("locationForecastDetailed")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (todaysDay == day) "I dag $dateFormatted" else "$day $dateFormatted",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$daysHighestTemp°\n${daysLowestTemp}°",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(4.dp)
                )

                if (visibleBoxesCount == 4) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "00-06")
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "00:00:00",
                                date = date
                            )
                        }
                    }
                }
                if (visibleBoxesCount >= 3) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "06-12")
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "06:00:00",
                                date = date
                            )
                        }
                    }
                }
                if (visibleBoxesCount >= 2) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "12-18")
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "12:00:00",
                                date = date
                            )
                        }
                    }
                }
                if (visibleBoxesCount >= 1) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "18-00")
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "18:00:00",
                                date = date
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun daysHighestTemp(homeUIState: HomeScreenUIState, date: String): Double {
     return homeUIState.forecast?.properties?.timeseries
         ?.filter { it.time.startsWith(date) }
         ?.maxOfOrNull { it.data.instant.details.air_temperature } ?: Double.MIN_VALUE
}

private fun daysLowestTemp(homeUIState: HomeScreenUIState, date: String): Double {
    return homeUIState.forecast?.properties?.timeseries
        ?.filter { it.time.startsWith(date) }
        ?.minOfOrNull { it.data.instant.details.air_temperature } ?: Double.MAX_VALUE
}