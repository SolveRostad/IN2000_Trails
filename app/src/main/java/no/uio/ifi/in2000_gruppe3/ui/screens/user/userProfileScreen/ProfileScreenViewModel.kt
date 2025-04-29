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
import no.uio.ifi.in2000_gruppe3.data.user.ProfileRepository


class UserScreenViewModel(application: Application):AndroidViewModel(application) {
    private val profileRepository: ProfileRepository
    private val _userScreenUIState = MutableStateFlow<UserScreenUIState>(
        UserScreenUIState()
    )

    val UserScreenUIState: StateFlow<UserScreenUIState> = _userScreenUIState.asStateFlow()


    init {
        val applicationScope = CoroutineScope(SupervisorJob())
        profileRepository = ProfileRepository.getInstance(application, applicationScope)
        getAllUsers()
    }

    fun addUser(username: String) {
        viewModelScope.launch {
            _userScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                val newProfile = Profile (username = username)
                profileRepository.addUser(newProfile)
                Log.e("CurrentUsers", "Current users: ${_userScreenUIState.value.profiles}")
                Log.d("UserScreenViewModel", "Legger til bruker ${newProfile.username}")
                _userScreenUIState.update {
                    it.copy (profiles = _userScreenUIState.value.profiles + newProfile)
                }
            } catch (e: Exception) {
                Log.e("UserScreenViewModel", "Error adding user: ${e.message}")
                _userScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error adding user: ${e.message}",
                        isError = true
                    )
                }
            } finally {
                _userScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }

    fun deleteUser(username: String) {
        viewModelScope.launch {
            _userScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                val userToDelete = _userScreenUIState.value.profiles.find { it.username == username }
                if (userToDelete != null) {
                    profileRepository.deleteUser(userToDelete)
                    _userScreenUIState.update {
                        it.copy (profiles = _userScreenUIState.value.profiles - userToDelete)
                    }
                }
            } catch (e: Exception) {
                _userScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error deleting user: ${e.message}",
                        isError = true
                    )
                }
            } finally {
                _userScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }
        }
    }

    fun selectUser(username: String) {
        viewModelScope.launch {
            _userScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                profileRepository.selectUser(username)
                _userScreenUIState.update {
                    it.copy (username = username, selectedUser = username)
                }
                Log.d("UserScreenViewModel", "Selected user: ${_userScreenUIState.value.selectedUser}")
            } catch (e: Exception) {
                _userScreenUIState.update {
                    it.copy (
                        isLoading = false,
                        errorMessage = "Error selecting user: ${e.message}",
                        isError = true
                    )
                }
            } finally {
                _userScreenUIState.update {
                    it.copy (isLoading = true)
                }
            }
        }
    }

    fun unselectUser() {
        viewModelScope.launch {
            _userScreenUIState.update {
                it.copy (isLoading = true)
            }
            try {
                profileRepository.unselectUser()
                _userScreenUIState.update {
                    it.copy (username = "", selectedUser = "")
                }
            } catch (e: Exception) {
                _userScreenUIState.update {
                    it.copy (isLoading = false, errorMessage = "Error unslecting user: ${e.message}", isError = true)
                }
            } finally {
                _userScreenUIState.update {
                    it.copy (isLoading = false)                }
            }
        }
    }

//    fun getSelectedUser() {
//        viewModelScope.launch {
//            _userScreenUIState.update {
//                it.copy(
//                    selectedUser = userRepository.getSelectedUser()?.username ?: ""
//                )
//            }
//        }
//    }

    fun getAllUsers() {
        viewModelScope.launch {
            _userScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                _userScreenUIState.update {
                    it.copy (profiles = profileRepository.getAllUsers())
                }
            } catch (e: Exception) {
                _userScreenUIState.update{
                    it.copy (isLoading = false, errorMessage = "Error getting all users: ${e.message}")
                }
            } finally {
                _userScreenUIState.update {
                    it.copy (isLoading = false)
                }
            }

        }
    }
}

data class UserScreenUIState(
    val profiles: List<Profile> = emptyList(),
    val selectedUser: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false
)



