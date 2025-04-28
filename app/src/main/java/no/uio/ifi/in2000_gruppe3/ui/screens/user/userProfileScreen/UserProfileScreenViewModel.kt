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
import no.uio.ifi.in2000_gruppe3.data.database.User
import no.uio.ifi.in2000_gruppe3.data.database.UserFavoritesDatabase
import no.uio.ifi.in2000_gruppe3.data.user.UserRepository


class UserScreenViewModel(application: Application):AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val _userScreenUIState = MutableStateFlow<UserScreenUIState>(
        UserScreenUIState()
    )

    val UserScreenUIState: StateFlow<UserScreenUIState> = _userScreenUIState.asStateFlow()


    init {
        val applicationScope = CoroutineScope(SupervisorJob())
        val userDao = UserFavoritesDatabase.getDatabase(application, applicationScope).userDao()
        userRepository = UserRepository(userDao)
        getAllUsers()
    }

    fun addUser(username: String) {
        viewModelScope.launch {
            _userScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                val newUser = User (username = username)
                userRepository.addUser(newUser)
                Log.e("CurrentUsers", "Current users: ${_userScreenUIState.value.users}")
                Log.d("UserScreenViewModel", "Legger til bruker ${newUser.username}")
                _userScreenUIState.update {
                    it.copy (users = _userScreenUIState.value.users + newUser)
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
                val userToDelete = _userScreenUIState.value.users.find { it.username == username }
                if (userToDelete != null) {
                    userRepository.deleteUser(userToDelete)
                    _userScreenUIState.update {
                        it.copy (users = _userScreenUIState.value.users - userToDelete)
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
                userRepository.selectUser(username)
                _userScreenUIState.update {
                    it.copy (username = username, selectedUser = username)
                }
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
                userRepository.unselectUser()
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

    fun getSelectedUser() {
        viewModelScope.launch {
            _userScreenUIState.update {
                it.copy(
                    selectedUser = userRepository.getSelectedUser()?.username ?: ""
                )
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _userScreenUIState.update{
                it.copy (isLoading = true)
            }
            try {
                _userScreenUIState.update {
                    it.copy (users = userRepository.getAllUsers())
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
    val users: List<User> = emptyList(),
    val selectedUser: String = "",
    val username: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isError: Boolean = false
)



