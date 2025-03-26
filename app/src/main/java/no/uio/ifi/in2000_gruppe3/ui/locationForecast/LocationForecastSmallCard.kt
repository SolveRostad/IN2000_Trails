package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.data.date.getCurrentTime
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDate
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LocationForecastSmallCard(
    day: String,
    date: String,
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    val todaysDate = getTodaysDate()
    val currentTime = getCurrentTime()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        onClick = { navController.navigate("locationForecastDetailed")}
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = day,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (date > todaysDate || (date == todaysDate && currentTime < "06:00:00")) {
                    Box {
                        Text(text = "00-06")
                        ForecastDisplay(
                            homeScreenViewModel = homeScreenViewModel,
                            mapboxViewModel = mapboxViewModel,
                            timeseries = "00-06",
                            date = date
                        )
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "12:00:00")) {
                    Box {
                        Text(text = "06-12")
                        ForecastDisplay(
                            homeScreenViewModel = homeScreenViewModel,
                            mapboxViewModel = mapboxViewModel,
                            timeseries = "06-12",
                            date = date
                        )
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "18:00:00")) {
                    Box {
                        Text(text = "12-18")
                        ForecastDisplay(
                            homeScreenViewModel = homeScreenViewModel,
                            mapboxViewModel = mapboxViewModel,
                            timeseries = "12-18",
                            date = date
                        )
                    }
                }
                if (date > todaysDate || (date == todaysDate && currentTime < "23:59:00")) {
                    Box {
                        Text(text = "18-00")
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