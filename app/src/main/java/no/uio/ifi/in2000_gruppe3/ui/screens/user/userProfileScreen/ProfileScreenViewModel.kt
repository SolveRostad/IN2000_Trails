package no.uio.ifi.in2000_gruppe3.ui.screens.userProfileScreen

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
    private val _profileScreenUIState = MutableStateFlow<ProfileScreenUIState>(
        ProfileScreenUIState()
    )

    val ProfileScreenUIState: StateFlow<ProfileScreenUIState> = _profileScreenUIState.asStateFlow()


    init {
        val applicationScope = CoroutineScope(SupervisorJob())
        profileRepository = ProfileRepository.getInstance(application, applicationScope)
        setProfile()
        getAllProfiles()
    }

    fun addProfile(username: String) {
        viewModelScope.launch {
            _profileScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                val newProfile = Profile (username = username)
                profileRepository.addUser(newProfile)
                Log.e("CurrentUsers", "Current users: ${_profileScreenUIState.value.profiles}")
                Log.d("UserScreenViewModel", "Legger til bruker ${newProfile.username}")
                _profileScreenUIState.update {
                    it.copy (profiles = _profileScreenUIState.value.profiles + newProfile)
                }
            } catch (e: Exception) {
                Log.e("UserScreenViewModel", "Error adding user: ${e.message}")
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error adding user: ${e.message}",
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
                val userToDelete = _profileScreenUIState.value.profiles.find { it.username == username }
                if (userToDelete != null) {
                    profileRepository.deleteUser(userToDelete)
                    _profileScreenUIState.update {
                        it.copy (profiles = _profileScreenUIState.value.profiles - userToDelete)
                    }
                }
            } catch (e: Exception) {
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error deleting user: ${e.message}",
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

    fun selectProfile(username: String) {
        viewModelScope.launch {
            _profileScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                profileRepository.selectUser(username)
                _profileScreenUIState.update {
                    it.copy (username = username, selectedUser = username)
                }
                Log.d("UserScreenViewModel", "Selected user: ${_profileScreenUIState.value.selectedUser}")
            } catch (e: Exception) {
                _profileScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error selecting user: ${e.message}",
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

    fun setProfile() {
        viewModelScope.launch {
            try {
                val selected = profileRepository.getSelectedUser()
                _profileScreenUIState.update {
                    it.copy(
                        username = selected.username,
                        selectedUser = selected.username
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileScreenViewModel", "Error getting selected Profile: ${e.message}")
            }
        }
    }

    fun getAllProfiles() {
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
                    it.copy (isLoading = false, errorMessage = "Error getting all users: ${e.message}")
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
    val selectedUser: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false
)



