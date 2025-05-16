package no.uio.ifi.in2000_gruppe3.ui.screens.startup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.delay
import no.uio.ifi.in2000_gruppe3.ui.screens.profile.profileSelectScreen.ProfileScreenViewModel

@Composable
fun StartupScreen(
    profileScreenViewModel: ProfileScreenViewModel,
    onCheckComplete: (Boolean) -> Unit
) {
    val profileUIState by profileScreenViewModel.profileScreenUIState.collectAsState()

    LaunchedEffect(key1 = true) {
        delay(10)
        onCheckComplete(profileUIState.isLoggedIn)
    }
}