package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.models.Locationforecast
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.repository.LocationForecastRepository
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.MetAlerts
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.repository.MetAlertsRepository
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent

class HomeScreenViewModel() : ViewModel() {
    private val hikeAPIRepository = HikeAPIRepository()
    private val locationForecastRepository = LocationForecastRepository()
    private val metAlertsRepository = MetAlertsRepository()
    private val _sheetStateTarget = MutableStateFlow(SheetDrawerDetent.SEMIPEEK)

    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            hikes = emptyList(),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null,
        )
    )

    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()
    val sheetStateTarget: StateFlow<SheetDrawerDetent> = _sheetStateTarget.asStateFlow()

    fun setSheetState(target: SheetDrawerDetent) {
        _sheetStateTarget.value = target
    }

    fun fetchHikes(
        lat: Double,
        lng: Double,
        limit: Int,
        featureType: String,
        minDistance: Int
    ) {
        viewModelScope.launch {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val hikesResponse =
                    hikeAPIRepository.getHikes(lat, lng, limit, featureType, minDistance)
                _homeScreenUIState.update {
                    it.copy(hikes = hikesResponse, isError = false)
                }
            } catch (e: Exception) {
                _homeScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _homeScreenUIState.update {
                    it.copy(isLoading = false)
                }
                _sheetStateTarget.value = SheetDrawerDetent.PEEK
            }
        }
    }

    fun fetchForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
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
            } finally {
                _homeScreenUIState.update {
                    it.copy(isLoading = false)
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
    val hikes: List<Feature>,
    val alerts: MetAlerts,
    val forecast: Locationforecast?,
)