package no.uio.ifi.in2000.sondrein.in2000_gruppe3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mapbox.maps.MapView
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox.MapViewer
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.theme.IN2000_gruppe3Theme
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.Screen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.HikeRouteCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IN2000_gruppe3Theme {
                Surface() {
                    App()
                }
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    HikeAppNavHost(navController = navController)
}

@Composable
fun HikeAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onHikeClick = { hikeId ->
                    navController.navigate(Screen.HikeRoute.createRoute(hikeId))
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorites.route)
                }
            )
        }

        composable(
            route = Screen.HikeRoute.route,
            arguments = listOf(navArgument("hikeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hikeId = backStackEntry.arguments?.getInt("hikeId") ?: -1
            HikeRouteCard(
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

        composable(Screen.Favorites.route) {
            FavoriteScreen(
                onHikeClick = { hikeId ->
                    navController.navigate(Screen.HikeRoute.createRoute(hikeId))
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