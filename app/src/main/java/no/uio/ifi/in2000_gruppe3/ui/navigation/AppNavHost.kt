package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.favoriteScreen.FavoritesViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen.GeminiScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen.GeminiViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreen
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.locationForecast.LocationForecastScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // ViewModels
    val homeScreenViewModel: HomeScreenViewModel = viewModel()
    val hikeScreenViewModel: HikeScreenViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()
    val mapboxViewModel: MapboxViewModel = viewModel()
    val geminiViewModel: GeminiViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home screen
        composable(Screen.Home.route) {
            HomeScreen(
                homeScreenViewModel = homeScreenViewModel,
                hikeViewModel = hikeScreenViewModel,
                mapboxViewModel = mapboxViewModel,
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
                hikeScreenviewModel = hikeScreenViewModel,
                favoritesViewModel = favoritesViewModel,
                mapboxViewModel = mapboxViewModel,
                navController = navController
            )
        }

        // Location forecast screen
        composable(Screen.LocationForecast.route) {
            LocationForecastScreen(
                navController = navController
            )
        }

        // Gemini screen
        composable(Screen.Gemini.route) {
            GeminiScreen(
                geminiViewModel = geminiViewModel,
                navController = navController
            )
        }
    }
}