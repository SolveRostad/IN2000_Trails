package no.uio.ifi.in2000_gruppe3.ui.screens.profile.profileSelectScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.database.Profile
import no.uio.ifi.in2000_gruppe3.data.profile.repository.ProfileRepository

class ProfileScreenViewModel(application: Application):AndroidViewModel(application) {
    private val profileRepository: ProfileRepository

    private val _profileScreenUIState = MutableStateFlow(
        ProfileScreenUIState()
    )
    val profileScreenUIState: StateFlow<ProfileScreenUIState> = _profileScreenUIState.asStateFlow()

    init {
        val applicationScope = CoroutineScope(SupervisorJob())
        profileRepository = ProfileRepository.getInstance(application, applicationScope)
        viewModelScope.launch {
            setProfile()
            getAllProfiles()
        }
    }

    fun addProfile(username: String) {
        viewModelScope.launch {
            _profileScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                val newProfile = Profile(username = username)
                profileRepository.addUser(newProfile)
                _profileScreenUIState.update {
                    it.copy (profiles = _profileScreenUIState.value.profiles + newProfile)
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", "addProfile: ${e.message}")
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = e.message.toString(),
                        isError = true
                    )
                }
            } finally {
                _profileScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }

    fun deleteProfile(username: String) {
        viewModelScope.launch {
            _profileScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                val profileToDelete = _profileScreenUIState.value.profiles.find { it.username == username }
                if (profileToDelete != null) {
                    profileRepository.deleteProfile(profileToDelete)
                    _profileScreenUIState.update {
                        it.copy (profiles = _profileScreenUIState.value.profiles - profileToDelete)
                    }
                }
            } catch (e: Exception) {
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error deleting profile: ${e.message}",
                        isError = true
                    )
                }
            } finally {
                _profileScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }

    fun selectProfile(username: String, onUserSelected: () -> Unit = {}) {
        viewModelScope.launch {
            _profileScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                profileRepository.selectProfile(username)
                _profileScreenUIState.update {
                    it.copy (
                        username = username,
                        isLoggedIn = username != "defaultUser"
                    )
                }
                onUserSelected()
            } catch (e: Exception) {
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error selecting profile: ${e.message}",
                        isError = true
                    )
                }
            } finally {
                _profileScreenUIState.update {
                    it.copy (isLoading = true)
                }
            }
        }
    }

    private fun setProfile() {
        viewModelScope.launch {
            try {
                val selected = profileRepository.getSelectedProfile()
                _profileScreenUIState.update {
                    it.copy(
                        username = selected.username,
                        isLoggedIn = selected.username != "defaultUser"
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", "setProfile: ${e.message}")
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = e.message.toString(),
                        isError = true
                    )
                }
            } finally {
                _profileScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }

    private fun getAllProfiles() {
        viewModelScope.launch {
            _profileScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                _profileScreenUIState.update {
                    it.copy (profiles = profileRepository.getAllUsers())
                }
            } catch (e: Exception) {
                _profileScreenUIState.update{
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error getting all profiles: ${e.message}"
                    )
                }
            } finally {
                _profileScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }
}

data class ProfileScreenUIState(
    val profiles: List<Profile> = emptyList(),
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false,
    val isLoggedIn: Boolean = false
)
