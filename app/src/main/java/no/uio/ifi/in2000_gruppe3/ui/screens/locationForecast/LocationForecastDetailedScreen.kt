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
import no.uio.ifi.in2000_gruppe3.data.date.Weekdays
import no.uio.ifi.in2000_gruppe3.data.date.getCurrentTime
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.ShowForecastByHour
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

    // Date variables
    val todaysDay = getTodaysDay()
    val selectedDay = hikeUIState.day
    val currentTime = getCurrentTime()
    val currentHour = currentTime.substring(0, 2).toInt()
    val startHour = if (selectedDay == todaysDay) currentHour else 0

    // Calculate the difference from selected day to todays day
    val todayIndex = Weekdays.indexOf(todaysDay)
    val selectedIndex = Weekdays.indexOf(selectedDay)
    val daysAhead = if (selectedIndex >= todayIndex) {
        selectedIndex - todayIndex
    } else {
        7 - todayIndex + selectedIndex
    }

    val forecastUpdatedAt = homeUIState.forecast?.properties?.meta?.updated_at
    val forecastUpdatedAtHour = forecastUpdatedAt?.substring(11, 13)!!.toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    if (selectedDay == todaysDay) "I dag ${hikeUIState.formattedDate}"
                    else "$selectedDay ${hikeUIState.formattedDate}") },
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
                        listOf("Tid", "Vær", "Temp.", "Vind", "Luftfuktighet").forEach { header ->
                            Text(
                                text = header,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }

                    // Divider line
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

                    // Show forecast for each hour
                    (0..23).forEach { hour ->
                        val summaryKey = if (daysAhead < 3) "1_hour"
                                         else if (daysAhead == 3 && hour !in listOf(0, 6, 12, 18)) "1_hour"
                                         else "6_hours"
                        ShowForecastByHour(
                            hour = hour,
                            summaryKey = summaryKey,
                            homeViewModel = homeScreenViewModel,
                            hikeViewModel = hikeScreenViewModel
                        )
                    }
                }
            }
        }
    }
}
