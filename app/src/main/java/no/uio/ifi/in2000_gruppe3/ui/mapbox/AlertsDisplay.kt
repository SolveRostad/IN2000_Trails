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
import com.mapbox.geojson.Point
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import kotlin.math.*


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
    val mapCoords = mapboxUiState.pointerCoordinates //brukerens posisjon
    Log.d("AlertsDisplay", "User's mapCoords: $mapCoords")


    val uniqueAlerts = homeScreenUiState.alerts?.features?.distinctBy {it.properties.event }

    Log.d("AlertsDisplay", "Number of alerts: ${homeScreenUiState.alerts?.features?.size}")
    uniqueAlerts?.forEach {
        Log.d("AlertsDisplay", "Unique Alert: ${it.properties.event}")
    }

    //finn nærmeste varsel og regner ut avstanden
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
        Log.d("AlertsDisplay", "closestAlert = null, distance= $distance, Ingen varsler i nærheten")
        return // ikke vis noe
    }

    Log.d("AlertsDisplay", "Current coordinates: $closestAlert")

    val alertEvent = closestAlert.properties.event
    val alertText = closestAlert.properties.description  //bruker senere
    val alertColor = closestAlert.properties.riskMatrixColor

    Log.d("AlertsDisplay", "alertEvent: ${homeScreenUiState.alerts}")

    val iconURL = getAlertsIconUrl(alertEvent, alertColor) // Henter icon-URL
    Log.d("SVG-URL", "Henter ikon fra: $iconURL, distance = $distance")

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
