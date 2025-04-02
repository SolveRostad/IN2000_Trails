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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

val eventIconMap = mapOf(
    "Avalanches" to "icon-warning-avalanches",
    "BlowingSnow" to "icon-warning-snow",
    "DrivingConditions" to "icon-warning-drivingconditions",
    "Flood" to "icon-warning-flood",
    "ForestFire" to "icon-warning-forestfire",
    "Gale" to "icon-warning-wind",
    "Ice" to "icon-warning-ice",
    "Icing" to "icon-warning-generic",
    "Landslide" to "icon-warning-landslide",
    "PolarLow" to "icon-warning-polarlow",
    "Rain" to "icon-warning-rain",
    "RainFlood" to "icon-warning-rainflood",
    "Snow" to "icon-warning-snow",
    "StormSurge" to "icon-warning-stormsurge",
    "Lightning" to "icon-warning-lightning",
    "Wind" to "icon-warning-wind",
    "Unknown" to "icon-warning-generic",
    "Extreme" to "icon-warning-extreme"
)

fun getAlertsIconUrl(event: String?, riskMatrixColor: String?): String {
    val iconId = eventIconMap[event] ?: "icon-warning-generic" // Default hvis event ikke finnes

    if (riskMatrixColor.isNullOrEmpty()) {
        Log.d("AlertIcon", "RiskMatrixColor is null or empty!")
        return "https://example.com/default-icon.png"
    }
    val iconUrl = "https://raw.githubusercontent.com/nrkno/yr-warning-icons/master/design/svg/$iconId-$riskMatrixColor.svg".lowercase()

    Log.d("AlertIcon", "Icon URL: $iconUrl")
    return iconUrl
}

@Composable
fun AlertsDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel,
    modifier: Modifier
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val coordinates = mapboxUiState.pointerCoordinates //bruker senere

    Log.d("AlertsDisplay", "Current coordinates: $coordinates")


    if(homeScreenUiState.alerts != null){
        val firstTimeSeries = homeScreenUiState.alerts.features.firstOrNull()
        val alertEvent = firstTimeSeries?.properties?.event
        val alertText = firstTimeSeries?.properties?.description  //bruker senere
        val alertColor = firstTimeSeries?.properties?.riskMatrixColor

        Log.d("AlertsDisplay", "alertEvent: ${homeScreenUiState.alerts}")

        val iconURL = getAlertsIconUrl(alertEvent, alertColor) // Henter icon-URL
        Log.d("SVG-URL", "Henter ikon fra: $iconURL")

        // Coil med SVG-support
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconURL)
                .decoderFactory(SvgDecoder.Factory())
                .build()
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Image(
                painter = painter,
                contentDescription = "Varsel-ikon",
                modifier = Modifier.size(50.dp)
            )
            if(alertEvent != null){
                Text(text = alertEvent, style = MaterialTheme.typography.bodyMedium)
            }
            Box(modifier = Modifier.align(Alignment.CenterHorizontally), content = {
            })

        }
    }
}
