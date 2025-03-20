package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
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
    private var _routeColorIndex = 0

    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            turer = Turer(listOf(), ""),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null,
            pointerCoordinates = Point.fromLngLat( 10.661952, 59.846195)
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

    // Holder på style og darkmode til kartet
    private val _mapStyle = mutableStateOf("STANDARD")
    var mapStyle: String
        get() = _mapStyle.value
        set(value) {
            _mapStyle.value = value
        }
    private val _mapIsDarkmode = mutableStateOf(false)
    var mapIsDarkmode: Boolean
        get() = _mapIsDarkmode.value
        set(value) {
            _mapIsDarkmode.value = value
        }

    // Oppdaterer style og darkmode til kartet
    fun updateMapStyle(style: String, isDark: Boolean) {
        mapStyle = style
        mapIsDarkmode = isDark
    }

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
    fun updatePointerCoordinates(point: Point) {
        _homeScreenUIState.update {
            it.copy(pointerCoordinates = point)
        }
    }

    fun getViableRouteColor(): Color {
        if (_routeColorIndex == polylineColors.size-1) {
            _routeColorIndex = 0
        } else {
            _routeColorIndex++
        }
        return polylineColors[_routeColorIndex]
    }
}

data class HomeScreenUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val turer: Turer,
    val alerts: MetAlerts,
    val forecast: Locationforecast?,
    val pointerCoordinates: Point
)