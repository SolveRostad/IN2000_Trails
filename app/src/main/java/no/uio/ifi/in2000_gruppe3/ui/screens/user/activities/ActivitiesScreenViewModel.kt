package no.uio.ifi.in2000_gruppe3.ui.screens.user.activities

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.database.Favorite
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.log.repository.LogRepository
import no.uio.ifi.in2000_gruppe3.data.profile.repository.ProfileRepository
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

class ActivitiesScreenViewModel(
    application: Application,
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val hikeAPIRepository: HikeAPIRepository,
    private val mapboxViewModel: MapboxViewModel,
): AndroidViewModel(application) {


        private val _activitiesScreenUIState = MutableStateFlow<ActivitiesScreenUIState>(
        ActivitiesScreenUIState()
    )

    val activitiesScreenUIState: StateFlow<ActivitiesScreenUIState> = _activitiesScreenUIState.asStateFlow()

    val userPosition = mapboxViewModel.mapboxUIState.value.latestUserPosition

    init {
        viewModelScope.launch {
            try {
                setUser()
                getConvertedLog()
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error initializing data: ${e.message}")
            }
        }
    }

    fun loadFavorites(){
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val username = profileRepository.getSelectedUser().username
                val updatedLog = logRepository.getAllLogs(username)

                _activitiesScreenUIState.update {
                    it.copy(hikeLog = updatedLog)
                }

                Log.d("FavoritesViewModel", "Fetched favorites for user: ${username}: ${_activitiesScreenUIState.value.hikeLog}")
                getConvertedLog()

            } catch (e: Exception) {
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
                Log.e("FavoritesViewModel", "Error loading favorites: ${e.message}")
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun setUser() {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(username = profileRepository.getSelectedUser().username)
            }
        }
    }

    fun updateUserLocation(point: Point) {
        _activitiesScreenUIState.update {
            it.copy(userLocation = point)
        }
    }

    fun isHikeInLog(feature: Feature): Boolean {
        return _activitiesScreenUIState.value.hikeLog.contains(feature.properties.fid)
    }

    fun getConvertedLog() {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val logFeatures: List<Feature> = hikeAPIRepository.getHikesById(
                    _activitiesScreenUIState.value.hikeLog,
                    _activitiesScreenUIState.value.userLocation
                )
                _activitiesScreenUIState.update {
                    it.copy(convertedLog = logFeatures)
                }
                Log.d("FavoritesViewModel", "Fetched converted favorites: $logFeatures")
            } catch (e: Exception) {
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addToLog(id: Int) {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val currentUser = profileRepository.getSelectedUser()
                val newLog = no.uio.ifi.in2000_gruppe3.data.database.Log(currentUser.username, id)
                Log.d("FavoritesViewModel", "Adding favorite: $newLog")
                logRepository.addLog(newLog)
                _activitiesScreenUIState.update {
                    it.copy(
                        hikeLog = _activitiesScreenUIState.value.hikeLog + newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun revmoveFromLog(id: Int) {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val newLog= no.uio.ifi.in2000_gruppe3.data.database.Log(
                    _activitiesScreenUIState.value.username,
                    id
                )
                logRepository.deleteLog(newLog)

                _activitiesScreenUIState.update {
                    it.copy(
                        hikeLog = _activitiesScreenUIState.value.hikeLog - newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                Log.e("ActivitiesViewModel", "Error fetching Log: ${e.message}")
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addNotesToLog(hikeId: Int, notes: String) {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.addNotesToLog(_activitiesScreenUIState.value.username, hikeId, notes)
            } catch (e: Exception) {
                Log.e("ActivitiesViewModel", "Error adding notes to log: ${e.message}")
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun adjustTimesWalked (hikeId: Int, adjustTimesWalked: Int) {
        viewModelScope.launch {
            _activitiesScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.timesWalked(_activitiesScreenUIState.value.username, hikeId, adjustTimesWalked)
            } catch (e: Exception) {
                Log.e("ActivitiesViewModel", "Error adjusting times walked: ${e.message}")
                _activitiesScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activitiesScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}

data class ActivitiesScreenUIState(
    val convertedLog: List<Feature> = emptyList(),
    val username: String = "",
    val userLocation: Point = Point.fromLngLat(10.441649, 59.542819),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false,
    val hikeLog: List<Int> = emptyList()
)