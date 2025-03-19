package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            turer = Turer(listOf(), ""),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

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
}

data class HomeScreenUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val turer: Turer,
    val alerts: MetAlerts,
    val forecast: Locationforecast?
)