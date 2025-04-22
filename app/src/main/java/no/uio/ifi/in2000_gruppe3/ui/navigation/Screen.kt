package no.uio.ifi.in2000_gruppe3.ui.navigation

// Class for navController to navigate between screens
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Chatbot : Screen("chatbot")
    object Favorites : Screen("favorites")
    object HikeScreen : Screen("hikeScreen")
    object LocationForecast : Screen("locationForecast")
    object LocationForecastDetailed: Screen("locationForecastDetailed")
}