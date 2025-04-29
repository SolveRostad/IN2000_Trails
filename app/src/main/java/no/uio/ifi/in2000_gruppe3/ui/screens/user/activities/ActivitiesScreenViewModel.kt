//package no.uio.ifi.in2000_gruppe3.ui.screens.user.activities
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import no.uio.ifi.in2000_gruppe3.data.database.User
//import no.uio.ifi.in2000_gruppe3.data.user.UserRepository
//
//class ActivitiesScreenViewModel(application: Application): AndroidViewModel(application) {
//    private val userRepository: UserRepository
//    private val _activitiesScreenUIState = MutableStateFlow<ActivitiesScreenUIState>(
//        ActivitiesScreenUIState()
//    )
//
//    val activitiesScreenUIState: StateFlow<ActivitiesScreenUIState> = _activitiesScreenUIState.asStateFlow()
//
//    init {
//
//    }
//}
//
//data class ActivitiesScreenUIState(
//    val selectedUserUsername: String = "",
//    val isLoading: Boolean = false,
//    val errorMessage: String = "",
//    val isError: Boolean = false,
//    val hikeLog: List<Int> = emptyList()
//)