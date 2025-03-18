package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens

// Define the routes
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object HikeRoute : Screen("hike/{hikeId}") {
        fun createRoute(hikeId: Int) = "hike/$hikeId"
    }
    object Favorites : Screen("favorites")
}