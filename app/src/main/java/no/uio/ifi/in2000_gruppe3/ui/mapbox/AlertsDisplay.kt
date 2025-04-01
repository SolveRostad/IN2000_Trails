package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

fun getAlertsIconUrl(event: String?, riskMatrixColor: String?): String {
    val iconUrl = if (event.isNullOrEmpty() || riskMatrixColor.isNullOrEmpty()) {
        Log.d("AlertIcon", "Event or riskMatrixColor is null or empty!")
        "https://example.com/default-icon.png" // Fallback-ikon
    } else {
        "https://raw.githubusercontent.com/nrkno/yr-warning-icons/master/design/svg/icon-warning-$event-$riskMatrixColor.svg"
    }

    Log.d("AlertIcon", "Icon URL: $iconUrl")  // Logging for debugging
    return iconUrl
}

@Composable
fun AlertsDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    Log.d("AlertsDisplay", "alerts: ${homeScreenUiState.alerts}")


    if(homeScreenUiState.alerts != null){
        val firstTimeSeries = homeScreenUiState.alerts.features.firstOrNull()
        val alertEvent = firstTimeSeries?.properties?.event
        val alertText = firstTimeSeries?.properties?.description
        val alertColor = firstTimeSeries?.properties?.riskMatrixColor
        Log.d("AlertsDisplay", "alertEvent: ${homeScreenUiState.alerts}")
        val iconURL = getAlertsIconUrl(alertEvent, alertColor) // Henter icon-URL

        Box(
            modifier = Modifier.padding(10.dp)
        ) {
                Image(
                    painter = rememberAsyncImagePainter(iconURL),
                    contentDescription = "Varsel-ikon",
                    modifier = Modifier.size(80.dp)
                )
                if (alertText != null) {
                    Text(
                        text = alertText,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
