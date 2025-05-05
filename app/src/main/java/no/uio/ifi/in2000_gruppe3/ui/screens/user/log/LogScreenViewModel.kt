package no.uio.ifi.in2000_gruppe3.ui.screens.user.log

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.log.repository.LogRepository
import no.uio.ifi.in2000_gruppe3.data.profile.repository.ProfileRepository
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

class LogScreenViewModel(
    application: Application,
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val hikeAPIRepository: HikeAPIRepository,
    private val mapboxViewModel: MapboxViewModel,
): AndroidViewModel(application) {
    private val _logScreenUIState = MutableStateFlow<ActivitiesScreenUIState>(
        ActivitiesScreenUIState()
    )

    val logScreenUIState: StateFlow<ActivitiesScreenUIState> = _logScreenUIState.asStateFlow()

    val userPosition = mapboxViewModel.mapboxUIState.value.latestUserPosition

    init {
        viewModelScope.launch {
            try {
                setUser()
                getConvertedLog()
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error initializing data: ${e.message}")
            }
        }
    }

    fun loadLog(){
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val username = profileRepository.getSelectedUser().username
                val updatedLog = logRepository.getAllLogs(username)

                _logScreenUIState.update {
                    it.copy(hikeLog = updatedLog)
                }

                Log.d("LogScreenViewModel", "Fetched log for user: ${username}: ${_logScreenUIState.value.hikeLog}")
                getConvertedLog()

            } catch (e: Exception) {
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
                Log.e("LogScreenViewModel", "Error loading log: ${e.message}")
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun setUser() {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(username = profileRepository.getSelectedUser().username)
            }
        }
    }

    fun updateUserLocation(point: Point) {
        _logScreenUIState.update {
            it.copy(userLocation = point)
        }
    }

    fun getConvertedLog() {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val logFeatures: List<Feature> = hikeAPIRepository.getHikesById(
                    _logScreenUIState.value.hikeLog,
                    _logScreenUIState.value.userLocation
                )
                _logScreenUIState.update {
                    it.copy(convertedLog = logFeatures)
                }
                Log.d("LogScreenViewModel", "Fetched converted log: $logFeatures")
            } catch (e: Exception) {
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addToLog(hikeId: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val currentUser = profileRepository.getSelectedUser()
                val newLog = no.uio.ifi.in2000_gruppe3.data.database.Log(currentUser.username, hikeId)
                Log.d("LogScreenViewModel", "Adding log: $newLog")
                logRepository.addLog(newLog)
                _logScreenUIState.update {
                    it.copy(
                        hikeLog = _logScreenUIState.value.hikeLog + newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun removeFromLog(hikeId: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val newLog = no.uio.ifi.in2000_gruppe3.data.database.Log(
                    _logScreenUIState.value.username,
                    hikeId
                )
                logRepository.deleteLog(newLog)

                _logScreenUIState.update {
                    it.copy(
                        hikeLog = _logScreenUIState.value.hikeLog - newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching Log: ${e.message}")
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addNotesToLog(hikeId: Int, notes: String) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.addNotesToLog(_logScreenUIState.value.username, hikeId, notes)
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error adding notes to log: ${e.message}")
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun adjustTimesWalked (hikeId: Int, adjustTimesWalked: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.timesWalked(_logScreenUIState.value.username, hikeId, adjustTimesWalked)
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error adjusting times walked: ${e.message}")
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    suspend fun getNotesForHike(hikeId: Int): String {
        return withContext(Dispatchers.IO) {
            try {
                logRepository.getNotesForHike(_logScreenUIState.value.username, hikeId)
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching notes for hike: ${e.message}")
                ""
            }
        }
    }

    fun getTimesWalked(hikeId: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy (isLoading = true)
            }
            try{
                val timesWalked = logRepository.getTimesWalked(_logScreenUIState.value.username, hikeId)
                _logScreenUIState.update {
                    it.copy (hikeLog = _logScreenUIState.value.hikeLog + timesWalked)
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching times walked: ${e.message}")
                _logScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _logScreenUIState.update {
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