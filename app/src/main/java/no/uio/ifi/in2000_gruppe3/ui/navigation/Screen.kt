package no.uio.ifi.in2000_gruppe3.ui.navigation

/**
 * Class that represents the different screens in the app.
 * @param route the route of the screen
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object OpenAI : Screen("chatbot")
    object Favorites : Screen("favorites")
    object HikeScreen : Screen("hikeScreen")
    object LocationForecast : Screen("locationForecast")
    object LocationForecastDetailed: Screen("locationForecastDetailed")
}