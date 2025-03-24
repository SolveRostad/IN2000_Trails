package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.annotation.MapboxExperimental
import com.mapbox.geojson.Point
import com.mapbox.search.ApiType
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngine.Companion.createSearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import com.mapbox.search.common.IsoCountryCode
import com.mapbox.search.common.IsoLanguageCode
import com.mapbox.search.details.RetrieveDetailsOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models.Locationforecast
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.repository.LocationForecastRepository
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.models.MetAlerts
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.repository.MetAlertsRepository
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Turer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.repository.TurAPIRepository

class HomeScreenViewModel() : ViewModel() {
    private val turAPIRepository = TurAPIRepository()
    private val locationForecastRepository = LocationForecastRepository()
    private val metAlertsRepository = MetAlertsRepository()

    private val placeAutocomplete = PlaceAutocomplete.create()

    // UI state
    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            turer = Turer(listOf(), ""),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null,
            pointerCoordinates = Point.fromLngLat( 10.661952, 59.846195),
            mapStyle = "STANDARD",
            mapIsDarkmode = false,
            searchQuery = "",
            searchResponse = emptyList()
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

    // Oppdaterer style og darkmode til kartet
    fun updateMapStyle(style: String, isDark: Boolean) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(mapStyle = style, mapIsDarkmode = isDark)
            }
        }
    }

    // Oppdaterer pekerposisjonen
    fun updatePointerCoordinates(point: Point) {
        _homeScreenUIState.update {
            it.copy(pointerCoordinates = point)
        }
    }

    // Henter turer fra TurAPI
    fun fetchTurer(lat: Double, lng: Double, limit: Int) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val turerResponse = turAPIRepository.getTurer(lat, lng, limit)
                _homeScreenUIState.update {
                    it.copy(turer = turerResponse, isError = false)
                }
                turerResponse.features.forEach { println(it.properties.toString()) }
            } catch (e: Exception) {
                _homeScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _homeScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _homeScreenUIState.update { it.copy(searchQuery = query) }

        if (query.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val suggestions = placeAutocomplete.suggestions(query)
                    Log.d("SearchBarViewModel", "Suggestion : ${suggestions.value}")

                    if (suggestions.isValue) {
                        _homeScreenUIState.update {
                            it.copy(searchResponse = suggestions.value ?: emptyList())
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SearchBarViewModel", "Error fetching suggestions", e)
                }
            }
        } else {
            _homeScreenUIState.update { it.copy(searchResponse = emptyList()) }
        }
    }

    fun getSelectSearchResultPoint(suggestion: PlaceAutocompleteSuggestion) {
        var point: Point = Point.fromLngLat(0.0, 0.0)
        viewModelScope.launch {
            try {
                val detailsResponse = placeAutocomplete.select(suggestion)
                Log.d("SearchBarViewModel", "Selected suggestion coordinates: ${detailsResponse.value?.coordinate}")
                val coordinates = detailsResponse.value!!.coordinate.coordinates()
                point = Point.fromLngLat(coordinates[0], coordinates[1])

                _homeScreenUIState.update {
                    it.copy(searchResponse = emptyList(), searchQuery = "", pointerCoordinates = point)
                }
            } catch (e: Exception) {
                Log.e("SearchBarViewModel", "Error selecting place", e)
            }
        }
    }

    fun fetchForecast(lat: Double, lon: Double){
        //Log.d("Forecast", "fetchForecast called with lat: $lat, lon: $lon")
        viewModelScope.launch(Dispatchers.IO) { // <-- Kjør direkte i IO-tråd
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val result = locationForecastRepository.getForecast(lat, lon)
                _homeScreenUIState.update {
                    it.copy(forecast = result, isError = false, isLoading = false)
                }
            } catch (e: Exception) {
                _homeScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error", isLoading = false)
                }
            }
        }
    }

    fun fetchAlerts(){
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val result = metAlertsRepository.getAlerts() //henter data fra repository
                if(result != null){
                    _homeScreenUIState.update {
                        it.copy(alerts = result)
                    }
                }
            } catch (e: Exception){
                _homeScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error", isLoading = false)
                }
            }
        }
    }
}

data class HomeScreenUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val turer: Turer,
    val alerts: MetAlerts,
    val forecast: Locationforecast?,
    val pointerCoordinates: Point,
    val mapStyle: String,
    val mapIsDarkmode: Boolean,
    val searchQuery: String,
    val searchResponse: List<PlaceAutocompleteSuggestion>,
)