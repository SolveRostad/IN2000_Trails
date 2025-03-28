package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import java.util.Locale

@Composable
fun ShowForecastByHour(
    hour: Int,
    summaryKey: String,
    homeViewModel: HomeScreenViewModel,
    hikeViewModel: HikeScreenViewModel,
) {
    val homeUIState = homeViewModel.homeScreenUIState.collectAsState()
    val hikeUIState = hikeViewModel.hikeScreenUIState.collectAsState()

    val formattedHour = String.format(Locale("nb", "NO"), "%02d:00", hour)
    val formattedHMS = String.format(Locale("nb", "NO"), "%02d:00:00", hour)

    val forecast = homeUIState.value.forecast?.properties?.timeseries?.find {
        it.time == "${hikeUIState.value.date}T${formattedHMS}Z"
    }

    val iconSymbolCode = when (summaryKey) {
        "1_hour" -> forecast?.data?.next_1_hours?.summary?.symbol_code
        "6_hours" -> forecast?.data?.next_6_hours?.summary?.symbol_code
        else -> null
    } ?: "--"

    val temperature = forecast?.data?.instant?.details?.air_temperature ?: "--"
    val windSpeed = forecast?.data?.instant?.details?.wind_speed ?: "--"
    val humidity = forecast?.data?.instant?.details?.relative_humidity ?: "--"

    if (temperature != "--") {
        LocationForecastByHour(
            tid = formattedHour,
            icon = iconSymbolCode.toString(),
            temperature = temperature.toString(),
            windSpeed = windSpeed.toString(),
            humidity = humidity.toString()
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
    }
}