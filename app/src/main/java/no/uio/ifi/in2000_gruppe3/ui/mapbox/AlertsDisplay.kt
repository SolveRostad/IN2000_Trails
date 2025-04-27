package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.mapbox.geojson.Point
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import kotlin.math.*

fun getAlertsIconUrl(event: String?, riskMatrixColor: String?): String {
    if (event.isNullOrEmpty() || riskMatrixColor.isNullOrEmpty()) {
        Log.d("AlertsDisplay", "getAlertsIconUrl: event eller color")
        return "null"
    }
    val eventCode = getEventCode(event)
    return "https://raw.githubusercontent.com/nrkno/yr-warning-icons/master/design/svg/icon-warning-$eventCode-$riskMatrixColor.svg"
}

@Composable
fun AlertsDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val mapCoords = mapboxUiState.pointerCoordinates

    val showAlertInfo = remember { mutableStateOf(false) }

    val uniqueAlerts = homeScreenUiState.alerts?.features?.distinctBy {it.properties.event }

    val closestAlertWithDistance = uniqueAlerts
        ?.mapNotNull { feature ->
            val coords = feature.geometry.getFirstCoordinate()
            coords?.let {
                val distance = haversineDistance(it, mapCoords)
                Pair(feature, distance)
            }
        }
        ?.minByOrNull { it.second }

    val closestAlert = closestAlertWithDistance?.first
    val distance = closestAlertWithDistance?.second

    if (closestAlert == null || distance == null) {
        return
    }

    val alertEvent = closestAlert.properties.event?.lowercase()
    val alertColor = closestAlert.properties.riskMatrixColor?.lowercase()

    if (distance < 400.0) {
        Column {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(6.dp)
                    .clickable { showAlertInfo.value = !showAlertInfo.value }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(getAlertsIconUrl(alertEvent, alertColor))
                            .decoderFactory(SvgDecoder.Factory())
                            .build()
                    ),
                    contentDescription = "Alert icon",
                    modifier = Modifier.size(48.dp)
                )
            }

            if (showAlertInfo.value) {
                Surface(
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = """
                            ${closestAlert.properties.area ?: "Ukjent område"}
                            ${closestAlert.properties.description ?: "Ingen beskrivelse tilgjengelig"}
                            Konsekvenser: ${closestAlert.properties.consequences ?: "Ingen konsekvenser tilgjengelig"}
                            Alvorlighetsgrad: ${closestAlert.properties.severity ?: "Ukjent alvorlighetsgrad"}
                            Risiko: ${closestAlert.properties.certainty ?: "Ukjent risiko"}
                            """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

// Returns the event code based on the event name
fun getEventCode(event: String?): String? {
    return when (event) {
        "blowingsnow" -> "snow"
        "gale" -> "wind"
        "icing" -> "generic"
        "unknown" -> "generic"
        else -> event
    }
}


fun Geometry.getFirstCoordinate(): Pair<Double, Double>? {
    //avhengig av om vi har et polygon eller multipolygon, returnerer vi første koordinat
    return when (this) {
        is Geometry.Polygon -> {
            // sjekker om første koordinat finnes og returner den som et pair
            this.coordinates.firstOrNull()?.firstOrNull()?.let {
                Pair(it[0], it[1])
            }
        }
        is Geometry.MultiPolygon -> {
            //henter første Polygon, deretter første liste, så koordinater
            this.coordinates.firstOrNull()?.firstOrNull()?.firstOrNull()?.let {
                Pair(it[0], it[1])
            }
        }

    }
}

//beregner avstand mellom to koordinater med haversine formulen
fun haversineDistance(coord1: Pair<Double, Double>, coord2: Point?): Double {
    val R = 6371.0 // jordens radius i km


    // Konverterer MetAlertkoordinatene til radianer
    //trenger ikke å sjekke om coord1 er null fordi funksjonen hadde blitt brutt før den kom til
    //haversineDistance
    val lat1 = Math.toRadians(coord1.second) // latitude
    val lon1 = Math.toRadians(coord1.first) // longitude

    //konverterer Mapboxkoordinatene til radianer. må sjekke om coord2 er null siden det ikke blir
    //sjekket noen andre steder
    if (coord2 == null) {
        return Double.MAX_VALUE
    }
    val lat2 = Math.toRadians(coord2.latitude())
    val lon2 = Math.toRadians(coord2.longitude())

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return R * c // Avstand i km
}
