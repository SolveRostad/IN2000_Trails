package no.uio.ifi.in2000_gruppe3.ui.screens.locationForecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.data.date.getCurrentTime
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.LocationForecastByHour
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationForecastDetailedScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    navController: NavHostController
) {
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()
    val homeUIState by homeScreenViewModel.homeScreenUIState.collectAsState()

    val currentTime = getCurrentTime()
    val currentHour = currentTime.substring(0, 2).toInt()
    val todaysDay = getTodaysDay()
    val selectedDay = hikeUIState.day
    val startHour = if (selectedDay == todaysDay) currentHour else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$selectedDay ${hikeUIState.formattedDate}") },
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
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        ) {
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tid", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Vær", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text(text = "Temp.", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }

                    // Divider line
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

                    // Tegner location forecast for hver time
                    for (hour in startHour..23) {
                        val formattedHour = String.format("%02d:00", hour)
                        val forecast = homeUIState.forecast?.properties?.timeseries
                            ?.firstOrNull { it.time.substring(11, 13) == formattedHour.substring(0, 2) }

                        val iconSymbolCode = forecast?.data?.next_1_hours?.summary?.symbol_code ?: ""

                        LocationForecastByHour(
                            tid = formattedHour,
                            temperature = forecast?.data?.instant?.details?.air_temperature?.toString() ?: "--",
                            icon = iconSymbolCode
                        )

                        // Divider between rows
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}