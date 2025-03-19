package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

/**
 * Class that represents the different screens in the app.
 * @param route the route of the screen
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object HikeCard : Screen("hike/{hikeId}") {
        fun createRoute(feature: Feature) = "hike/${feature.id}"
    }

    object Favorites : Screen("favorites")
}