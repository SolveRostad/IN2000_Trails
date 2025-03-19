package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation

/**
 * Class that represents the different screens in the app.
 * @param route the route of the screen
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object HikeCard : Screen("hike/{hikeId}") { fun createRoute(hikeId: Int) = "hike/$hikeId" }
    object Favorites : Screen("favorites")
}