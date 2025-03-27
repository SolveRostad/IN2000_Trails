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
    val todaysDate = getTodaysDate()
    val dateFormatted = getDateFormatted(date)
    val currentTime = getCurrentTime()
    val todaysDay = getTodaysDay()

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
            hikeScreenViewModel.updateDate(day, dateFormatted)
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
            ) {
                if (date > todaysDate || (date == todaysDate && currentTime < "06:00:00")) {
                    Box(
                        modifier = Modifier
                            .then(if (visibleBoxesCount == 4) Modifier.weight(1f) else Modifier)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "00-06")
                            Spacer(modifier = Modifier.height(10.dp))
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "00-06",
                                date = date
                            )
                        }
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "12:00:00")) {
                    Box(
                        modifier = Modifier
                            .then(if (visibleBoxesCount == 4) Modifier.weight(1f) else Modifier)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "06-12")
                            Spacer(modifier = Modifier.height(10.dp))
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "06-12",
                                date = date
                            )
                        }
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "18:00:00")) {
                    Box(
                        modifier = Modifier
                            .then(if (visibleBoxesCount == 4) Modifier.weight(1f) else Modifier)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "12-18")
                            Spacer(modifier = Modifier.height(10.dp))
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "12-18",
                                date = date
                            )
                        }
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "23:59:00")) {
                    Box(
                        modifier = Modifier
                            .then(if (visibleBoxesCount == 4) Modifier.weight(1f) else Modifier)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "18-00")
                            Spacer(modifier = Modifier.height(10.dp))
                            ForecastDisplay(
                                homeScreenViewModel = homeScreenViewModel,
                                mapboxViewModel = mapboxViewModel,
                                timeseries = "18-00",
                                date = date
                            )
                        }
                    }
                }
            }
        }
    }
}