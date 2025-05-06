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
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository.HikeAPIRepository
import no.uio.ifi.in2000_gruppe3.data.log.repository.LogRepository
import no.uio.ifi.in2000_gruppe3.data.profile.repository.ProfileRepository
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

class ActivityScreenViewModel(
    application: Application,
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val hikeAPIRepository: HikeAPIRepository,
    private val mapboxViewModel: MapboxViewModel,
): AndroidViewModel(application) {
    private val _activityScreenUIState = MutableStateFlow<ActivityScreenUIState>(
        ActivityScreenUIState()
    )

    val activityScreenUIState: StateFlow<ActivityScreenUIState> = _activityScreenUIState.asStateFlow()

    val userPosition = mapboxViewModel.mapboxUIState.value.latestUserPosition

    init {
        viewModelScope.launch {
            try {
                setUser()
                getConvertedActivities()
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error initializing data: ${e.message}")
            }
        }
    }

    fun loadActivities() {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val username = profileRepository.getSelectedUser().username
                val updatedLog = logRepository.getAllLogs(username)

                _activityScreenUIState.update {
                    it.copy(hikeLog = updatedLog)
                }

                Log.d("LogScreenViewModel", "Fetched log for user: ${username}: ${_activityScreenUIState.value.hikeLog}")
                getConvertedActivities()

            } catch (e: Exception) {
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
                Log.e("LogScreenViewModel", "Error loading log: ${e.message}")
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun setUser() {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(username = profileRepository.getSelectedUser().username)
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
        _activityScreenUIState.update {
            it.copy(userLocation = point)
        }
    }

    fun getConvertedActivities() {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val logFeatures: List<Feature> = hikeAPIRepository.getHikesById(
                    _activityScreenUIState.value.hikeLog,
                    _activityScreenUIState.value.userLocation
                )
                _activityScreenUIState.update {
                    it.copy(convertedLog = logFeatures)
                }
                calculateTotalDistance()
                Log.d("LogScreenViewModel", "Fetched converted log: $logFeatures")
            } catch (e: Exception) {
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addToActivityLog(hikeId: Int) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val currentUser = profileRepository.getSelectedUser()
                val newLog = no.uio.ifi.in2000_gruppe3.data.database.Log(currentUser.username, hikeId)
                Log.d("LogScreenViewModel", "Adding log: $newLog")
                logRepository.addLog(newLog)
                _activityScreenUIState.update {
                    it.copy(
                        hikeLog = _activityScreenUIState.value.hikeLog + newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun removeFromActivityLog(hikeId: Int) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val newLog = no.uio.ifi.in2000_gruppe3.data.database.Log(
                    _activityScreenUIState.value.username,
                    hikeId
                )
                logRepository.deleteLog(newLog)

                _activityScreenUIState.update {
                    it.copy(
                        hikeLog = _activityScreenUIState.value.hikeLog - newLog.hikeId
                    )
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching Log: ${e.message}")
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun addNotesToActivityLog(hikeId: Int, notes: String) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.addNotesToLog(_activityScreenUIState.value.username, hikeId, notes)
                _activityScreenUIState.update { state ->
                    state.copy(hikeNotes = state.hikeNotes + (hikeId to notes))
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error adding notes to log: ${e.message}")
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun adjustTimesWalked(hikeId: Int, adjustTimesWalked: Int) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                logRepository.adjustTimesWalked(_activityScreenUIState.value.username, hikeId, adjustTimesWalked)

                val updatedTimesWalked = logRepository.getTimesWalkedForHike(_activityScreenUIState.value.username, hikeId)
                _activityScreenUIState.update {
                    it.copy(hikeTimesWalked = it.hikeTimesWalked + (hikeId to updatedTimesWalked))
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error adjusting times walked: ${e.message}")
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun getNotesForHike(hikeId: Int) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val notes = logRepository.getNotesForHike(_activityScreenUIState.value.username, hikeId)
                _activityScreenUIState.update { state ->
                    state.copy(hikeNotes = state.hikeNotes + (hikeId to notes))
                }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching notes for hike: ${e.message}")
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun getTotalTimesWalked() {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy (isLoading = true)
            }
            try{
                val username = _activityScreenUIState.value.username
                val total = logRepository.getTotalTimesWalked(username)
                _activityScreenUIState.update { it.copy(hikesDone = total) }
            } catch (e: Exception) {
                Log.e("LogScreenViewModel", "Error fetching times walked: ${e.message}")
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun getTimesWalkedForHike(hikeId: Int) {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val timesWalked = logRepository.getTimesWalkedForHike(_activityScreenUIState.value.username, hikeId)

                _activityScreenUIState.update {
                    it.copy(hikeTimesWalked = it.hikeTimesWalked + (hikeId to timesWalked))
                }
            } catch(e: Exception) {
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    fun calculateTotalDistance() {
        viewModelScope.launch {
            _activityScreenUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val totalDistance = _activityScreenUIState.value.convertedLog.sumOf { feature ->
                    val hikeId = feature.properties.fid
                    val timesWalked = _activityScreenUIState.value.hikeTimesWalked[hikeId] ?: 1
                    (feature.properties.distance_meters / 1000.0) * timesWalked
                }
                _activityScreenUIState.update {
                    it.copy(totalDistance = totalDistance)
                }
            } catch (e: Exception) {
                _activityScreenUIState.update {
                    it.copy(isError = true, errorMessage = e.message.toString())
                }
            } finally {
                _activityScreenUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }
}

data class ActivityScreenUIState(
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