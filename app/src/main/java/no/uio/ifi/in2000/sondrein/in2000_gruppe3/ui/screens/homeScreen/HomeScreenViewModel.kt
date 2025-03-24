package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
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

    // UI state
    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            turer = Turer(listOf(), ""),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null,
            pointerCoordinates = Point.fromLngLat( 10.661952, 59.846195),
            searchQuery = "",
            searchResponse = emptyList(),
            mapStyle = "STANDARD",
            mapIsDarkmode = false
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

    // Fargehåndtering for ruter
    private var _routeColorIndex = 0 //dette bør endres/flyttes
    private val _routeColors = mutableMapOf<String, Color>()
    private val polylineColors = listOf(
        Color(0xFF3388FF), // Bright blue
        Color(0xFF32CD32), // Lime green
        Color(0xFFFF8C00), // Dark orange
        Color(0xFFE91E63), // Pink
        Color(0xFF9C27B0), // Purple
        Color(0xFF00BCD4), // Cyan
        Color(0xFFFF5252), // Red
        Color(0xFF795548), // Brown
        Color(0xFF607D8B), // Blue grey
        Color(0xFFFFEB3B)  // Yellow
    )

    // Returnerer farge for en rute, eller genererer ny farge hvis ruten ikke har en farge
    fun getViableRouteColor(featureId: String): Color {
        _routeColors[featureId]?.let { return it }

        val color = polylineColors[_routeColorIndex]
        if (_routeColorIndex == polylineColors.size-1) {
            _routeColorIndex = 0
        } else {
            _routeColorIndex++
        }

        _routeColors[featureId] = color
        return color
    }

    // Oppdaterer søkefeltet og henter forslag fra PlaceAutocomplete
    private val placeAutocomplete = PlaceAutocomplete.create()
    fun updateSearchQuery(query: String) { // putt i ny viewmodel?
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(searchQuery = query)
            }
            delay(500) // er dette en dum løsning for å unngå for mange requests?
            if (query.length >= 3) {
                val response = placeAutocomplete.suggestions(query)

                if (response.isValue) {
                    _homeScreenUIState.update {
                        it.copy(searchResponse = response.value ?: emptyList())
                    }
                } else {
                    Log.e("SearchBar", "Error fetching suggestions", response.error)
                }
            } else {
                _homeScreenUIState.update {
                    it.copy(searchResponse = emptyList())
                }
            }
        }
    }

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
    val searchQuery: String,
    val searchResponse: List<PlaceAutocompleteSuggestion>,
    val mapStyle: String,
    val mapIsDarkmode: Boolean
)