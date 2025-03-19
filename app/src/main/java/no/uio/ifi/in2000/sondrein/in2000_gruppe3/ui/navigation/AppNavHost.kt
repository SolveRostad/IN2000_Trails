package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.HikeCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home.HomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home screen
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel(),
                navController = navController
            )
        }

        // Favorites screen
        composable(Screen.Favorites.route) {
            FavoriteScreen(
                viewmodel = viewModel(),
                navController = navController
            )
        }

        // HikeCard screen
        composable(
            route = Screen.HikeCard.route,
            arguments = listOf(navArgument("hikeId") { type = NavType.IntType })
        ) {
            HikeCard(
                viewModel = viewModel(),
                navController = navController
            )
        }
    }
}