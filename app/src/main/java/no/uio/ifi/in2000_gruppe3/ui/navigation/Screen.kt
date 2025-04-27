package no.uio.ifi.in2000_gruppe3.ui.navigation

// Class for navController to navigate between screens
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object User : Screen("user")
    object Login : Screen("login")
    object Chatbot : Screen("chatbot")
    object Welcome : Screen("welcome")
    object Favorites : Screen("favorites")
    object HikeScreen : Screen("hikeScreen")
    object UserSettings : Screen("userSettings")
    object LocationForecast : Screen("locationForecast")
    object LocationForecastDetailed: Screen("locationForecastDetailed")
}