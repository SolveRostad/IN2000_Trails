package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

fun getWeatherIconUrl(symbolCode: String): String {
    val iconUrl: String
    if (symbolCode.isEmpty()) {
        Log.d("WeatherIcon", "symbolCode is null or empty!")
        iconUrl = "https://example.com/default-icon.png"  // Fallback URL
    } else {
        iconUrl = "https://raw.githubusercontent.com/metno/weathericons/refs/heads/main/weather/png/$symbolCode.png"
    }
    Log.d("WeatherIcon", "Icon URL: $iconUrl")  // Legg til logging for URL
    return iconUrl
}

@Composable
fun ForecastDisplay(viewModel: HomeScreenViewModel) {
    val uiState = viewModel.homeScreenUIState.collectAsState().value

    if (uiState.forecast != null) {
        val firstTimeseries = uiState.forecast.properties.timeseries.firstOrNull()
        val temperature = firstTimeseries?.data?.instant?.details?.air_temperature

        val symbolCode = firstTimeseries?.data?.next_1_hours?.summary?.symbol_code
        val iconURL = getWeatherIconUrl(symbolCode.toString())

        Box (
            modifier = Modifier.padding(5.dp, 40.dp),
            contentAlignment = Alignment.TopStart
        ){
            Image(
                painter = rememberAsyncImagePainter(iconURL),
                contentDescription = "Vær-ikon",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = if (temperature != null) {
                    "$temperature °C"
                } else {
                    "Ingen temperaturdata tilgjengelig"
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

