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
    val iconId = eventIconMap[event] ?: "icon-warning-generic" // Default hvis event ikke finnes

    if (riskMatrixColor.isNullOrEmpty()) {
        return "https://example.com/default-icon.png"
    }
    val iconUrl = "https://raw.githubusercontent.com/nrkno/yr-warning-icons/master/design/svg/$iconId-$riskMatrixColor.svg".lowercase()

    return iconUrl
}

@Composable
fun AlertsDisplay(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    val homeScreenUiState = homeScreenViewModel.homeScreenUIState.collectAsState().value
    val mapboxUiState = mapboxViewModel.mapboxUIState.collectAsState().value
    val mapCoords = mapboxUiState.pointerCoordinates //brukerens posisjon

    val uniqueAlerts = homeScreenUiState.alerts?.features?.distinctBy {it.properties.event }

    // Finn nærmeste varsel og regner ut avstanden
    val closestAlertWithDistance = uniqueAlerts
        ?.mapNotNull { feature ->
            val coords = feature.geometry.getFirstCoordinate()
            coords?.let {
                val distance = haversineDistance(it, mapCoords)
                Pair(feature, distance)
            }
        }
        ?.minByOrNull { it.second } // finn nærmeste

    val closestAlert = closestAlertWithDistance?.first
    val distance = closestAlertWithDistance?.second

    //hvis det ikke finnes noen varsler eller avstanden er for stor, vis ingenting
   if (closestAlert == null || distance == null || distance > 400.0) {
        return // ikke vis noe
    }

    val alertEvent = closestAlert.properties.event
    val alertTitle = closestAlert.properties.title
    val alertColor = closestAlert.properties.riskMatrixColor

    val iconURL = getAlertsIconUrl(alertEvent, alertColor) // Henter icon-URL

    // Coil med SVG-support
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(iconURL)
            .decoderFactory(SvgDecoder.Factory())
            .build()
    )

    val showAlertInfo = remember { mutableStateOf(false) }

    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    if (distance <= 400.0) {
                        showAlertInfo.value = !showAlertInfo.value
                    } else {
                        Log.d("AlertsDisplay", "Varsel er for langt unna!")
                    }
                }
        ) {
            Image(
                painter = painter,
                contentDescription = "Varsel-ikon",
                modifier = Modifier.size(50.dp)
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
                    text = alertTitle ?: "Ingen informasjon",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

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

val eventIconMap = mapOf(
    "avalanches" to "icon-warning-avalanches",
    "blowingSnow" to "icon-warning-snow",
    "drivingConditions" to "icon-warning-drivingconditions",
    "flood" to "icon-warning-flood",
    "forestFire" to "icon-warning-forestfire",
    "gale" to "icon-warning-wind",
    "ice" to "icon-warning-ice",
    "icing" to "icon-warning-generic",
    "landslide" to "icon-warning-landslide",
    "polarLow" to "icon-warning-polarlow",
    "rain" to "icon-warning-rain",
    "rainFlood" to "icon-warning-rainflood",
    "snow" to "icon-warning-snow",
    "stormSurge" to "icon-warning-stormsurge",
    "lightning" to "icon-warning-lightning",
    "wind" to "icon-warning-wind",
    "unknown" to "icon-warning-generic",
    "extreme" to "icon-warning-extreme"
)
