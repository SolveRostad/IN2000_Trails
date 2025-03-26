package no.uio.ifi.in2000_gruppe3.ui.locationForecast

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
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun LocationForecastSmallCard(
    dag: String,
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
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
                text = dag,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Box {
                    Text(text = "00-06")
                    OneHourForecastDisplay(homeScreenViewModel, mapboxViewModel)
                }
                Box {
                    Text(text = "06-12")
                    OneHourForecastDisplay(homeScreenViewModel, mapboxViewModel)
                }
                Box {
                    Text(text = "12-18")
                    OneHourForecastDisplay(homeScreenViewModel, mapboxViewModel)
                }
                Box {
                    Text(text = "18-00")
                    OneHourForecastDisplay(homeScreenViewModel, mapboxViewModel)
                }
            }

        }


    }
}