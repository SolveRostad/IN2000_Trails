package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDate
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapStyles
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
    timeseries: String = "instant", // instant som standard
    date: String = getTodaysDate(), // dagens dato som standard
    modifier: Modifier = Modifier
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val mapStyle = mapboxUiState.mapStyle

    if (homeScreenUiState.forecast != null) {

        val chosenTimeSeries = when (timeseries) {
            "instant" -> homeScreenUiState.forecast.properties.timeseries.firstOrNull()
            "00-06" -> homeScreenUiState.forecast.properties.timeseries.find { it.time == "${date}T06:00:00Z" }
            "06-12" -> homeScreenUiState.forecast.properties.timeseries.find { it.time == "${date}T12:00:00Z" }
            "12-18" -> homeScreenUiState.forecast.properties.timeseries.find { it.time == "${date}T18:00:00Z" }
            "18-00" -> homeScreenUiState.forecast.properties.timeseries.find { it.time == "${date}T20:00:00Z" }
            else -> homeScreenUiState.forecast.properties.timeseries.firstOrNull()
        }
        val temperature = chosenTimeSeries?.data?.instant?.details?.air_temperature
        val symbolCode = chosenTimeSeries?.data?.next_1_hours?.summary?.symbol_code
        val iconURL = getWeatherIconUrl(symbolCode.toString())
        val temperatureTextColor = if (mapStyle == MapStyles.STANDARD_SATELLITE) Color.White else Color.Black

        Box (
            modifier = modifier,
            contentAlignment = Alignment.TopStart
        ){
            if (temperature != null) {
                Image(
                    painter = rememberAsyncImagePainter(iconURL),
                    contentDescription = "Vær-ikon",
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopCenter)
                )

                Spacer(modifier = Modifier.height(65.dp))

                Text(
                    text = "$temperature°C",
                    style = MaterialTheme.typography.titleMedium,
                    color = temperatureTextColor,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}
