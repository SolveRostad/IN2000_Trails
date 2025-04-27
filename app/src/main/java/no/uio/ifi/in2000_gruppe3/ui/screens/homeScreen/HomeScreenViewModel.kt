package no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.models.Locationforecast
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.models.TimeSeries
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.repository.LocationForecastRepository
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.MetAlerts
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.repository.MetAlertsRepository
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel

class HomeScreenViewModel() : ViewModel() {
    private val hikeAPIRepository = HikeAPIRepository(openAIViewModel = OpenAIViewModel())
    private val locationForecastRepository = LocationForecastRepository()
    private val metAlertsRepository = MetAlertsRepository()

    private val _homeScreenUIState = MutableStateFlow<HomeScreenUIState>(
        HomeScreenUIState(
            hikes = emptyList(),
            alerts = MetAlerts(listOf(), "", "", ""),
            forecast = null,
        )
    )
    val homeScreenUIState: StateFlow<HomeScreenUIState> = _homeScreenUIState.asStateFlow()

    fun updateNetworkStatus(isConnected: Boolean) {
        _homeScreenUIState.update {
            it.copy(hasNetworkConnection = isConnected)
        }
    }

    private val _sheetStateTarget = MutableStateFlow(SheetDrawerDetent.SEMIPEEK)
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
                val hikesResponse = hikeAPIRepository.getHikes(lat, lng, limit, featureType, minDistance)
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
                _sheetStateTarget.value = SheetDrawerDetent.SEMIPEEK
            }
        }
    }

    suspend fun getRecommendedHikes(
        homeScreenViewModel: HomeScreenViewModel,
        mapBoxViewModel: MapboxViewModel,
        openAIViewModel: OpenAIViewModel
    ): List<Feature> {
        val hikes = hikeAPIRepository.getHikes(
            mapBoxViewModel.mapboxUIState.value.latestUserPosition?.latitude() ?: 59.856885,
            mapBoxViewModel.mapboxUIState.value.latestUserPosition?.longitude() ?: 10.660978,
            100,
            "Fotrute",
            500
        )
        return hikes.shuffled().take(3)
    }

    fun fetchForecast(point: Point) {
        val lat = point.latitude()
        val lng = point.longitude()

        viewModelScope.launch(Dispatchers.IO) {
            _homeScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val result = locationForecastRepository.getForecast(lat, lng)
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
                Log.d("FetchAlerts", "Alerts fetched: $result")

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

    fun clearHikes() {
        _homeScreenUIState.update { currentState ->
            currentState.copy(hikes = emptyList())
        }
    }

    fun timeSeriesFromDate(date: String): List<TimeSeries>? {
        return homeScreenUIState.value.forecast?.properties?.timeseries
            ?.filter { it.time.startsWith(date) }
    }

    fun daysHighestTemp(date: String): Double {
        return timeSeriesFromDate(date)
            ?.maxOfOrNull { it.data.instant.details.air_temperature } ?: Double.NEGATIVE_INFINITY
    }

    fun daysLowestTemp(date: String): Double {
        return timeSeriesFromDate(date)
            ?.minOfOrNull { it.data.instant.details.air_temperature } ?: Double.POSITIVE_INFINITY
    }

    fun daysAverageTemp(date: String): Double {
        val temps = timeSeriesFromDate(date)
            ?.map { it.data.instant.details.air_temperature }

        return temps?.average() ?: -1.0
    }

    fun daysAverageWindSpeed(date: String): Double {
        val windSpeeds = timeSeriesFromDate(date)
            ?.map { it.data.instant.details.wind_speed }

        return windSpeeds?.average() ?: -1.0
    }
}

data class HomeScreenUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val hikes: List<Feature>,
    val alerts: MetAlerts?,
    val forecast: Locationforecast?,
    val isLoggedIn: Boolean = true, // skal settes til false når login skjerm er laget
    val hasNetworkConnection: Boolean = true
)
