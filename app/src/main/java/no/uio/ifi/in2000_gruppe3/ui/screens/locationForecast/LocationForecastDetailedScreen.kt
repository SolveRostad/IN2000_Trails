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
                        listOf("Tid", "Vær", "Temp.", "Vind (m/s)", "Luftfuktighet (%)").forEach { header ->
                            Text(
                                text = header,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                modifier = when (header) {
                                    "Tid" -> Modifier.weight(0.8f).padding(start = 4.dp)
                                    "Temp" -> Modifier.weight(0.9f)
                                    "Vind (m/s)" -> Modifier.weight(1.3f)
                                    "Luftfuktighet (%)" -> Modifier.weight(2f)
                                    else -> Modifier.weight(0.8f)
                                }

                            )
                        }
                    }

                    // Divider line
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

                    // Hours based on days ahead
                    val hours = when {
                        daysAhead < 2 -> (startHour..23)
                        daysAhead == 2 -> (startHour..17)
                        else -> listOf(0, 6, 12, 18)
                    }

                    // Show forecast for each hour
                    hours.forEach { hour ->
                        val summaryKey = if (daysAhead < 3) "next_1_hours.summary.symbol_code" else "next_6_hours.summary.symbol_code"
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
