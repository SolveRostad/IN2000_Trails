package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.rememberIconImage
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

/**
 * MapViewer er en composable som viser et kart med mulighet for å velge kartstil og lysmåte
 */
@Composable
fun MapViewer(homeScreenViewModel: HomeScreenViewModel) {
    val uiState by homeScreenViewModel.homeScreenUIState.collectAsState()
    var mapStyle = uiState.mapStyle
    var mapIsDarkmode = uiState.mapIsDarkmode

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(12.0)
            center(uiState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
    }

    // Oppdaterer ViewportState når pekerposisjonen endres
    LaunchedEffect(uiState.pointerCoordinates, mapStyle) {
        mapViewportState.setCameraOptions {
            zoom(12.0)
            center(uiState.pointerCoordinates)
            pitch(0.0)
            bearing(0.0)
        }
        homeScreenViewModel.fetchTurer(
            uiState.pointerCoordinates.latitude(),
            uiState.pointerCoordinates.longitude(),
            5
        )
    }

    Box {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            style = {
                when (mapStyle) {
                    "STANDARD" -> {
                        MapboxStandardStyle {
                            lightPreset =
                                if (mapIsDarkmode) LightPresetValue.NIGHT else LightPresetValue.DAY
                        }
                    }
                    "SATELLITE" -> {
                        MapStyle(style = Style.STANDARD_SATELLITE)
                    }
                    "OUTDOORS" -> {
                        MapStyle(style = Style.OUTDOORS)
                    }
                }
            },
            onMapClickListener = { point ->
                homeScreenViewModel.updatePointerCoordinates(point)
                homeScreenViewModel.fetchTurer(point.latitude(), point.longitude(), 5)
                true
            },
            // Fjerner skala, logo og attribusjon fra kartet
            scaleBar = {},
            logo = {},
            attribution = {}
        ) {
            // Legger til turer på kartet
            MapEffect(uiState.turer) { mapView ->
                val mapboxMap = mapView.mapboxMap
                val style = mapboxMap.style

                // Fjerner tidligere turer
                if (style != null) {
                    // Fjern alle eksisterende kilder og lag for turer
                    style.styleSources.forEach { source ->
                        if (source.id.startsWith("source-")) {
                            style.removeStyleSource(source.id)
                        }
                    }
                    style.styleLayers.forEach { layer ->
                        if (layer.id.startsWith("layer-")) {
                            style.removeStyleLayer(layer.id)
                        }
                    }

                    // Legger til nye turer
                    uiState.turer.features.forEach { feature ->
                        val sourceId = "source-${feature.hashCode()}"
                        val layerId = "layer-${feature.hashCode()}"

                        val lineStrings = feature.geometry.coordinates.map { coords ->
                            val points = coords.map { Point.fromLngLat(it[1], it[0]) }
                            com.mapbox.geojson.LineString.fromLngLats(points)
                        }

                        val multiLineString = com.mapbox.geojson.MultiLineString.fromLineStrings(lineStrings)
                        val geoJsonFeature = com.mapbox.geojson.Feature.fromGeometry(multiLineString)
                        val featureCollection = com.mapbox.geojson.FeatureCollection.fromFeatures(listOf(geoJsonFeature))

                        val source = GeoJsonSource.Builder(sourceId)
                            .data(featureCollection.toJson())
                            .build()
                        style.addSource(source)

                        val lineLayer = LineLayer(layerId, sourceId)

                        lineLayer.lineColor(feature.color)
                        lineLayer.lineWidth(3.0)
                        lineLayer.lineOpacity(0.8)
                        style.addLayer(lineLayer)
                    }
                }
            }

            // Legger til markør for pekerposisjonen
            val marker = rememberIconImage(R.drawable.red_marker)
            PointAnnotation(point = uiState.pointerCoordinates) {
                iconImage = marker
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Temperature display
            ForecastDisplay(homeScreenViewModel)

            // Search bar
            SearchBarForMap(homeScreenViewModel, modifier = Modifier.padding(top = 10.dp))

            // Settings dropdown
            MapStyleDropdownMenu(homeScreenViewModel)
        }
    }
}
