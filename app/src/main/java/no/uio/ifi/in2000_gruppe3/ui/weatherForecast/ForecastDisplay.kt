package no.uio.ifi.in2000_gruppe3.ui.weatherForecast

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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

@Composable
fun ForecastDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val mapStyle = mapboxUiState.mapStyle

    if (homeScreenUiState.forecast != null) {
        val firstTimeseries = homeScreenUiState.forecast.properties.timeseries.firstOrNull()
        val temperature = firstTimeseries?.data?.instant?.details?.air_temperature

        val symbolCode = firstTimeseries?.data?.next_1_hours?.summary?.symbol_code
        val iconURL = getWeatherIconUrl(symbolCode.toString())

        val temperatureTextColor = if (mapStyle == MapStyles.STANDARD_SATELLITE) Color.White else Color.Black

        Box (
            modifier = Modifier.padding(10.dp, 20.dp),
            contentAlignment = Alignment.TopStart
        ){
            if (temperature != null) {
                Image(
                    painter = rememberAsyncImagePainter(iconURL),
                    contentDescription = "Vær-ikon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 10.dp)
                )
                Text(
                    text = "$temperature°C",
                    style = MaterialTheme.typography.titleMedium,
                    color = temperatureTextColor,
                    modifier = Modifier
                        .align(Alignment.BottomCenter) // Plasserer teksten i bunnen av boksen
                        .padding(top = 50.dp)
                )
            }
        }
    }
}
