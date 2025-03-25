package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.MultiLineString
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.turAPI.models.Feature

class MapboxViewModel() : ViewModel() {
    private val placeAutocomplete = PlaceAutocomplete.create()

    private val _mapboxUIState: MutableStateFlow<MapboxUIState> = MutableStateFlow(
        MapboxUIState(
            mapStyle = MapStyle.OUTDOORS,
            pointerCoordinates = Point.fromLngLat(10.661952, 59.846195),
            lineLayers = emptyList(),
            searchResponse = emptyList(),
            searchQuery = "",
        )
    )
    var mapboxUIState: StateFlow<MapboxUIState> = _mapboxUIState

    fun updateMapStyle(style: MapStyle) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(mapStyle = style)
            }
        }
    }

    fun updateLineStringsFromFeatures(features: List<Feature>, style: Style?) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(isLoading = true, lineLayers = emptyList())
            }
            val lineLayers: MutableList<LineLayer> = mutableListOf<LineLayer>()

            if (style == null) {
                return@launch
            }

            // Remove all existing sources and layers
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
            // Add new sources and layers
            features.forEach { feature ->
                val sourceId = "source-${feature.hashCode()}"
                val layerId = "layer-${feature.hashCode()}"

                val lineStrings = feature.geometry.coordinates.map { coords ->
                    val points = coords.map { Point.fromLngLat(it[1], it[0]) }
                    LineString.fromLngLats(points)
                }

                val multiLineString = MultiLineString.fromLineStrings(lineStrings)
                val geoJsonFeature = com.mapbox.geojson.Feature.fromGeometry(multiLineString)
                val featureCollection = FeatureCollection.fromFeatures(listOf(geoJsonFeature))

                val source = GeoJsonSource.Builder(sourceId)
                    .data(featureCollection.toJson())
                    .build()
                style.addSource(source)

                val lineLayer = LineLayer(layerId, sourceId)

                lineLayer.lineColor(feature.color)
                lineLayer.lineWidth(3.0)
                lineLayer.lineOpacity(0.8)

                lineLayers.add(lineLayer)
                style.addLayer(lineLayer)
            }

            _mapboxUIState.update {
                it.copy(isLoading = false, lineLayers = lineLayers)
            }
        }
    }

    fun updatePointerCoordinates(point: Point) {
        _mapboxUIState.update {
            it.copy(pointerCoordinates = point)
        }
    }

    fun updateSearchQuery(query: String) {
        _mapboxUIState.update { it.copy(searchQuery = query) }

        if (query.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val suggestions = placeAutocomplete.suggestions(query)
                    Log.d("SearchBarViewModel", "Suggestion : ${suggestions.value}")

                    if (suggestions.isValue) {
                        _mapboxUIState.update {
                            it.copy(searchResponse = suggestions.value ?: emptyList())
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SearchBarViewModel", "Error fetching suggestions", e)
                }
            }
        } else {
            _mapboxUIState.update { it.copy(searchResponse = emptyList()) }
        }
    }

    fun getSelectedSearchResultPoint(suggestion: PlaceAutocompleteSuggestion) {
        var point: Point = Point.fromLngLat(0.0, 0.0)
        viewModelScope.launch {
            try {
                val detailsResponse = placeAutocomplete.select(suggestion)
                Log.d(
                    "SearchBarViewModel",
                    "Selected suggestion coordinates: ${detailsResponse.value?.coordinate}"
                )
                val coordinates = detailsResponse.value!!.coordinate.coordinates()
                point = Point.fromLngLat(coordinates[0], coordinates[1])

                _mapboxUIState.update {
                    it.copy(
                        searchResponse = emptyList(),
                        searchQuery = "",
                        pointerCoordinates = point
                    )
                }
            } catch (e: Exception) {
                Log.e("SearchBarViewModel", "Error selecting place", e)
            }
        }
    }
}

data class MapboxUIState(
    val mapStyle: MapStyle,
    val pointerCoordinates: Point,
    val lineLayers: List<LineLayer>,

    val searchResponse: List<PlaceAutocompleteSuggestion>,
    val searchQuery: String,

    val isLoading: Boolean = false
)

enum class MapStyle {
    STANDARD,
    SATELLITE,
    OUTDOORS
}