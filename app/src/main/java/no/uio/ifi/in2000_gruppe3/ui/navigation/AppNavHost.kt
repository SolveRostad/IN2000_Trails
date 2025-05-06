package no.uio.ifi.in2000_gruppe3.ui.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.locationForecast.LocationForecastDetailedScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.locationForecast.LocationForecastScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.ChatbotScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesScreenViewModelFactory
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.WelcomeScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.user.UserScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.user.UserSettingsScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.user.log.LogScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.user.log.LogScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.user.log.LogScreenViewModelFactory
import no.uio.ifi.in2000_gruppe3.ui.screens.user.userProfileScreen.ProfileScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.user.userProfileScreen.ProfileScreenViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // ViewModels
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val hikeScreenViewModel: HikeScreenViewModel = viewModel()
    val favoritesViewModel: FavoritesScreenViewModel = viewModel(
        factory = FavoritesScreenViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            openAIViewModel = OpenAIViewModel()
        )
    )
    val mapboxViewModel: MapboxViewModel = viewModel()
    val openAIViewModel: OpenAIViewModel = viewModel()
    val profileScreenViewModel: ProfileScreenViewModel = viewModel()
    val logScreenViewModel: LogScreenViewModel = viewModel(
        factory = LogScreenViewModelFactory(
            application = LocalContext.current.applicationContext as Application,
            openAIViewModel = OpenAIViewModel()
        )
    )

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        // Welcome screen
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                navController = navController
            )
        }

        // Home screen
        composable(Screen.Home.route) {
            HomeScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                favoritesViewModel = favoritesViewModel,
                mapboxViewModel = mapboxViewModel,
                openAIViewModel = openAIViewModel,
                navController = navController
            )
        }

        // Favorites screen
        composable(Screen.Favorites.route) {
            FavoriteScreen(
                favoritesViewModel = favoritesViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
                navController = navController
            )
        }

        // HikeCard screen
        composable(Screen.HikeScreen.route) {
            HikeScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                favoritesViewModel = favoritesViewModel,
                mapboxViewModel = mapboxViewModel,
                openAIViewModel = openAIViewModel,
                logScreenViewModel = logScreenViewModel,
                navController = navController
            )
        }

        // Location forecast screen
        composable(Screen.LocationForecast.route) {
            LocationForecastScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                navController = navController
            )
        }

        // Location forecast detailed screen
        composable(Screen.LocationForecastDetailed.route) {
            LocationForecastDetailedScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                navController = navController
            )
        }

        // Chatbot screen
        composable(Screen.Chatbot.route) {
            ChatbotScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
                navController = navController
            )
        }

        // User screen
        composable(Screen.User.route) {
            UserScreen(
                hikeScreenViewModel = hikeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
                logScreenViewModel = logScreenViewModel,
                profileScreenViewModel = profileScreenViewModel,
                navController = navController
            )
        }

        // User settings screen
        composable(Screen.UserSettings.route) {
             UserSettingsScreen(
                 mapboxViewModel = mapboxViewModel,
                 profileScreenViewModel = profileScreenViewModel,
                 navController = navController
             )
        }

        // User profile screen
        composable(Screen.UserProfile.route) {
            ProfileScreen(
                profileScreenViewModel = profileScreenViewModel,
                navController = navController
            )
        }

        // Logged hikes screen
        composable(Screen.Log.route) {
            LogScreen(
                logScreenViewModel = logScreenViewModel,
                hikeScreenViewModel = hikeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
                navController = navController
            )
        }
    }
}
