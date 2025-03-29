package no.uio.ifi.in2000_gruppe3.ui.screens.locationForecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.data.date.Weekdays
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDate
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.locationForecast.LocationForecastSmallCard
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationForecastScreen(
    homeScreenViewModel: HomeScreenViewModel,
    hikeScreenViewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()

    // Variables for weekdays
    val todaysDay = getTodaysDay()
    val startIndex = Weekdays.indexOf(todaysDay)
    val weekdaysList = Weekdays.entries.toList()
    val orderedWeekdays = weekdaysList.drop(startIndex) + weekdaysList.take(startIndex)

    // Date
    val todaysDateStr = getTodaysDate()
    val todaysDate = LocalDate.parse(todaysDateStr)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(hikeUIState.feature.properties.desc ?: "Ukjent rutenavn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item {
                orderedWeekdays.forEachIndexed { index, day ->
                    val date = todaysDate.plusDays(index.toLong())
                    val formattedDate = date.toString()

                    LocationForecastSmallCard(
                        day = day.toString(),
                        date = formattedDate,
                        homeScreenViewModel = homeScreenViewModel,
                        hikeScreenViewModel = hikeScreenViewModel,
                        mapboxViewModel = mapboxViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}