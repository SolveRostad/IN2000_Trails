package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun ForecastDisplay(viewModel: HomeScreenViewModel) {
    val uiState = viewModel.homeScreenUIState.collectAsState().value

    //Log.d("Forecast", "Forecast data: ${uiState.forecast}")
    if (uiState.forecast != null) {

        val firstTimeseries = uiState.forecast.properties.timeseries.firstOrNull()

        // Hent temperatur hvis tilgjengelig, ellers vis en fallback tekst
        val temperature = firstTimeseries?.data?.instant?.details?.air_temperature
        val temperatureUnit = uiState.forecast.properties.meta.units.air_temperature ?: "--"

        Box(
            modifier = Modifier
                //.background(Color.White.copy(alpha = 0.7f))
                .padding(80.dp)
        ) {
            Text(
                text = if (temperature != null) {
                    "$temperature $temperatureUnit°C"
                } else {
                    "Ingen temperaturdata tilgjengelig"
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}