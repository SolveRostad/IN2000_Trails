package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapboxViewModel() : ViewModel() {
    private val placeAutocomplete = PlaceAutocomplete.create()

    private val _mapboxUIState: MutableStateFlow<MapboxUIState> = MutableStateFlow(
        MapboxUIState(
            mapStyle = Style.OUTDOORS,
            pointerCoordinates = Point.fromLngLat(10.661952, 59.846195),
            lineLayers = emptyList(),
            searchResponse = emptyList(),
            searchQuery = "",
        )
    )
    var mapboxUIState: StateFlow<MapboxUIState> = _mapboxUIState

    fun updateMapStyle(style: String) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(mapStyle = style)
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
        viewModelScope.launch {
            try {
                val detailsResponse = placeAutocomplete.select(suggestion)
                Log.d(
                    "SearchBarViewModel",
                    "Selected suggestion coordinates: ${detailsResponse.value?.coordinate}"
                )
                val coordinates = detailsResponse.value!!.coordinate.coordinates()
                val point = Point.fromLngLat(coordinates[0], coordinates[1])

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

    fun setLoaderState(isLoading: Boolean) {
        viewModelScope.launch {
            _mapboxUIState.update {
                it.copy(isLoading = isLoading)
            }
        }
    }
}

data class MapboxUIState(
    val mapStyle: String,
    val pointerCoordinates: Point,
    val lineLayers: List<LineLayer>,

    val searchResponse: List<PlaceAutocompleteSuggestion>,
    val searchQuery: String,

    val isLoading: Boolean = false
)