package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mapbox.maps.Style
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.coordinates.Coordinates

/**
 * Presenterer et kart med mulighet for å velge kartstil og lysmåte
 * Kun en preview som må endres på for å matche funksjonalitet i appen
 */
@Composable
fun MapViewer() {
    val defaultCoordinates = Coordinates(59.9138688, 10.7522454) // Oslo
    var coordinates by remember { mutableStateOf(defaultCoordinates) }
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf(Style.STANDARD) }
    var zoom by remember { mutableDoubleStateOf(12.0) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(zoom)
            center(Point.fromLngLat(defaultCoordinates.lon, defaultCoordinates.lat))
            pitch(0.0)
            bearing(0.0)
        }
    }

    /**
     * LaunchedEffect for å oppdatere kartet når koordinatene endres
     */
    LaunchedEffect(coordinates) {
        zoom = mapViewportState.cameraState?.zoom ?: 12.0
        mapViewportState.setCameraOptions {
            center(Point.fromLngLat(coordinates.lon, coordinates.lat))
            zoom(zoom)
        }
    }

    Box {
        MapboxMap(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
                //.clip(RoundedCornerShape(12.dp)),
            mapViewportState = mapViewportState,
            style = {
                if (selectedStyle == Style.STANDARD) {
                    MapboxStandardStyle {
                        lightPreset = if (isDarkMode) LightPresetValue.NIGHT else LightPresetValue.DAY
                    }
                } else {
                    MapStyle(style = selectedStyle)
                }
            },
            onMapClickListener = { point ->
                // Hva som skjer når man trykker på kartet

                // Henter koordinatene til punktet som ble trykket på og oppdaterer koordinatene
                val newCoordinates = Coordinates(point.latitude(), point.longitude())
                coordinates = newCoordinates
                // Returnerer koordinatene til stedet som ble trykket på
                //onCoordinatesSelected(newCoordinates)
                true
            },
            // Fjerner skala, logo og attribusjon fra kartet
            scaleBar = {},
            logo = {},
            attribution = {}
        ) {
            // Hva som skal vises på kartet

            // Legger til en markør på kartet
            val marker = rememberIconImage(R.drawable.red_marker)
            PointAnnotation(point = Point.fromLngLat(coordinates.lon, coordinates.lat)) {
                iconImage = marker
            }
        }

        // Legger til knapper for å velge kartstil og lysmåte
        Column(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.7f))
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    MapButton("Standard") { selectedStyle = Style.STANDARD }
                    Spacer(modifier = Modifier.width(4.dp))
                    MapButton("Satellite") { selectedStyle = Style.STANDARD_SATELLITE }
                    Spacer(modifier = Modifier.width(4.dp))
                    MapButton("Outdoors") { selectedStyle = Style.OUTDOORS }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (selectedStyle == Style.STANDARD) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        MapButton("Light") { isDarkMode = false }
                        Spacer(modifier = Modifier.width(4.dp))
                        MapButton("Dark") { isDarkMode = true }
                    }
                }
            }
        }
    }
}
