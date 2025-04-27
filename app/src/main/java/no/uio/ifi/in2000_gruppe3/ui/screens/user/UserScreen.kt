package no.uio.ifi.in2000_gruppe3.ui.screens.user

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import no.uio.ifi.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    homeScreenViewModel: HomeScreenViewModel,
    navController: NavHostController,
) {
    val homeUIState by homeScreenViewModel.homeScreenUIState.collectAsState()

    var currentView by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        if (!homeUIState.isLoggedIn) {
            navController.navigate(Screen.Login.route)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Min side",
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.UserSettings.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Row {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { currentView = 0 }
                    ) {
                        Text(
                            text = "Aktiviteter",
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { currentView = 1 }
                    ) {
                        Text(
                            text = "Profil",
                        )
                    }
                }
            }

            item {
                when (currentView) {
                    0 -> { Aktiviteter() }
                    1 -> { Profil() }
                }
            }
        }
    }
}