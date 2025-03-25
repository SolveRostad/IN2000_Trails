package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.models.Locationforecast
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.repository.LocationForecastRepository
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.MetAlerts
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.repository.MetAlertsRepository
import no.uio.ifi.in2000_gruppe3.data.turAPI.models.Hikes
import no.uio.ifi.in2000_gruppe3.data.turAPI.repository.TurAPIRepository

class HomeScreenViewModel() : ViewModel() {
    private val turAPIRepository = TurAPIRepository()
    private val locationForecastRepository = LocationForecastRepository()
    private val metAlertsRepository = MetAlertsRepository()


    // UI state
    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            hikes = Hikes(listOf(), ""),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

    // Henter turer fra TurAPI
    fun fetchHikes(lat: Double, lng: Double, limit: Int) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val turerResponse = turAPIRepository.getTurer(lat, lng, limit)
                _homeScreenUIState.update {
                    it.copy(hikes = turerResponse, isError = false)
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


    fun fetchForecast(lat: Double, lon: Double) {
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
                    it.copy(
                        isError = true,
                        errorMessage = e.message ?: "Unknown error",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun fetchAlerts() {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val result = metAlertsRepository.getAlerts() //henter data fra repository
                if (result != null) {
                    _homeScreenUIState.update {
                        it.copy(alerts = result)
                    }
                }
            } catch (e: Exception) {
                _homeScreenUIState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.message ?: "Unknown error",
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class HomeScreenUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val hikes: Hikes,
    val alerts: MetAlerts,
    val forecast: Locationforecast?
)