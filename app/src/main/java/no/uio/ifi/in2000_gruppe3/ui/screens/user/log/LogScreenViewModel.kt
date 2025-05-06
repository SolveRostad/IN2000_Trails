package no.uio.ifi.in2000_gruppe3.ui.screens.user.log

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
    private val _logScreenUIState = MutableStateFlow<LogScreenUIState>(
        LogScreenUIState()
    )

    val logScreenUIState: StateFlow<LogScreenUIState> = _logScreenUIState.asStateFlow()

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
                it.copy(isLoading = true)
            }
            try {
                val selectedUser = profileRepository.getSelectedUser()
                _logScreenUIState.update {
                    it.copy(
                        username = selectedUser.username,
                        hikeLog = emptyList(),
                        hikeTimesWalked = emptyMap(),
                        hikeNotes = emptyMap(),
                        convertedLog = emptyList(),
                        hikesDone = 0,
                        totalDistance = 0.0,
                        isLoading = true
                    )
                }

                val userLogs = logRepository.getAllLogs(selectedUser.username)

                _logScreenUIState.update {
                    it.copy(
                        hikeLog = userLogs,
                        isLoading = false
                    )
                }

                getConvertedLog()
                getTotalTimesWalked()

                userLogs.forEach { hikeId ->
                    getTimesWalkedForHike(hikeId)
                    getNotesForHike(hikeId)
                }

                calculateTotalDistance()
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error setting user: ${e.message}")
                _logScreenUIState.update {
                    it.copy(isLoading = false, isError = true, errorMessage = e.message ?: "Unknown error")
                }
            } finally {
                _logScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun updateUserLocationFromMapbox() {
        val latestPosition = mapboxViewModel.mapboxUIState.value.latestUserPosition
        if(latestPosition != null) {
            updateUserLocation(latestPosition)
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
                calculateTotalDistance()
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
                _logScreenUIState.update { state ->
                    state.copy(hikeNotes = state.hikeNotes + (hikeId to notes))
                }
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
                logRepository.adjustTimesWalked(_logScreenUIState.value.username, hikeId, adjustTimesWalked)

                val updatedTimesWalked = logRepository.getTimesWalkedForHike(_logScreenUIState.value.username, hikeId)
                _logScreenUIState.update {
                    it.copy(hikeTimesWalked = it.hikeTimesWalked + (hikeId to updatedTimesWalked))
                }
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

    fun getNotesForHike(hikeId: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val notes = logRepository.getNotesForHike(_logScreenUIState.value.username, hikeId)
                _logScreenUIState.update { state ->
                    state.copy(hikeNotes = state.hikeNotes + (hikeId to notes))
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching notes for hike: ${e.message}")
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

    fun getTotalTimesWalked() {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy (isLoading = true)
            }
            try{
                val username = _logScreenUIState.value.username
                val total = logRepository.getTotalTimesWalked(username)
                _logScreenUIState.update { it.copy(hikesDone = total) }
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

    fun getTimesWalkedForHike(hikeId: Int) {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val timesWalked = logRepository.getTimesWalkedForHike(_logScreenUIState.value.username, hikeId)

                _logScreenUIState.update {
                    it.copy(hikeTimesWalked = it.hikeTimesWalked + (hikeId to timesWalked))
                }
            } catch(e: Exception) {
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

    fun calculateTotalDistance() {
        viewModelScope.launch {
            _logScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val totalDistance = _logScreenUIState.value.convertedLog.sumOf { feature ->
                    val hikeId = feature.properties.fid
                    val timesWalked = _logScreenUIState.value.hikeTimesWalked[hikeId] ?: 0
                    (feature.properties.distance_meters / 1000.0) * timesWalked
                }
                _logScreenUIState.update {
                    it.copy(totalDistance = totalDistance)
                }
            } catch(e: Exception) {
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

data class LogScreenUIState(
    val hikeNotes: Map<Int, String> = emptyMap(),
    val convertedLog: List<Feature> = emptyList(),
    val username: String = "",
    val userLocation: Point = Point.fromLngLat(10.441649, 59.542819),
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false,
    val hikeLog: List<Int> = emptyList(),
    val hikesDone: Int = 0,
    val hikeTimesWalked: Map<Int, Int> = emptyMap(),
    val totalDistance: Double = 0.0
)