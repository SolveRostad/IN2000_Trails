package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun ForecastDisplay(
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.homeScreenUIState.collectAsState().value

    if (uiState.forecast != null) {
        val firstTimeseries = uiState.forecast.properties.timeseries.firstOrNull()

        // Hent temperatur hvis tilgjengelig, ellers vis en fallback tekst
        val temperature = firstTimeseries?.data?.instant?.details?.air_temperature
        val temperatureUnit = uiState.forecast.properties.meta.units.air_temperature

        Log.d("Forecast", "Temperature: $temperature")

        Box(
            modifier = modifier.padding(5.dp, 40.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = if (temperature != null) "$temperature°C" else "",
                style = MaterialTheme.typography.titleMedium,
                color = if (uiState.mapIsDarkmode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}