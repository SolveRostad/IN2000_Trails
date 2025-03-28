package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.mapbox.maps.Style
import no.uio.ifi.in2000_gruppe3.data.date.getCurrentTime
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDate
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

fun getWeatherIconUrl(symbolCode: String): String {
    val iconUrl: String
    if (symbolCode.isEmpty()) {
        Log.d("WeatherIcon", "symbolCode is null or empty!")
        iconUrl = "https://example.com/default-icon.png"  // Fallback URL
    } else {
        iconUrl = "https://raw.githubusercontent.com/metno/weathericons/refs/heads/main/weather/png/$symbolCode.png"
    }
    Log.d("WeatherIcon", "Icon URL: $iconUrl")  //logging for URL
    return iconUrl
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    modifier: Modifier = Modifier,
    timeseries: String = "instant", // instant som standard
    date: String = getTodaysDate(), // dagens dato som standard
    visableOnMap: Boolean = false,
    showTemperature: Boolean = true
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val mapStyle = mapboxUiState.mapStyle

    val currentTime = getCurrentTime()
    val todaysDate = getTodaysDate()

    if (homeScreenUiState.forecast != null) {

        val chosenTimeSeries = when (timeseries) {
            "instant" -> homeScreenUiState.forecast.properties.timeseries.firstOrNull()
            else -> {
                if (todaysDate == date && currentTime > timeseries) {
                    homeScreenUiState.forecast.properties.timeseries.firstOrNull()
                } else {
                    homeScreenUiState.forecast.properties.timeseries.find { it.time == "${date}T${timeseries}Z" }
                }
            }
        }

        val temperature = chosenTimeSeries?.data?.instant?.details?.air_temperature
        val temperatureTextColor = if (mapStyle == Style.STANDARD_SATELLITE && visableOnMap) Color.White else Color.Black

        val symbolCode = when (timeseries) {
            "instant" -> chosenTimeSeries?.data?.next_1_hours?.summary?.symbol_code
            else -> chosenTimeSeries?.data?.next_6_hours?.summary?.symbol_code
        }
        val iconURL = getWeatherIconUrl(symbolCode.toString())

        Column(
            modifier = if (visableOnMap) modifier.fillMaxWidth()
                       else Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (temperature != null) {
                Image(
                    painter = rememberAsyncImagePainter(iconURL),
                    contentDescription = "Vær-ikon",
                    modifier = Modifier
                        .size(60.dp)
                )
                if (showTemperature) {
                    Text(
                        text = "$temperature°C",
                        style = MaterialTheme.typography.titleMedium,
                        color = temperatureTextColor,
                    )
                }
            }
        }
    }
}
