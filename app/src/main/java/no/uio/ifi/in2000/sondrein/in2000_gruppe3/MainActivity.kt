package no.uio.ifi.in2000.sondrein.in2000_gruppe3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.HikeCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.Screen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.theme.IN2000_gruppe3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IN2000_gruppe3Theme {
                Surface {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home screen
        composable(Screen.Home.route) {
            HomeScreen(
                onHikeClick = { hikeId ->
                    navController.navigate(Screen.HikeCard.createRoute(hikeId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        // HikeCard screen
        composable(
            route = Screen.HikeCard.route,
            arguments = listOf(navArgument("hikeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hikeId = backStackEntry.arguments?.getInt("hikeId") ?: -1
            HikeCard(
                hikeId = hikeId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFavoriteClick = {
                    navController.navigate(Screen.Favorites.route) {
                        // Pop up to Home screen, but don't include it
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // Favorites screen
        composable(Screen.Favorites.route) {
            FavoriteScreen(
                onHikeClick = { hikeId ->
                    navController.navigate(Screen.HikeCard.createRoute(hikeId))
                },
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        // Pop up to start destination and include it
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}