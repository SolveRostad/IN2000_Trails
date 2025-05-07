package no.uio.ifi.in2000_gruppe3.ui.navigation

// Class for navController to navigate between screens
sealed class Screen(val route: String) {

    object Home : Screen("home")
    object User : Screen("user")
    object Chatbot : Screen("chatbot")
    object Welcome : Screen("welcome")
    object Activity: Screen("activity")
    object Profile: Screen("userProfile")
    object Favorites : Screen("favorites")
    object MapPreview : Screen("mapPreview")
    object HikeScreen : Screen("hikeScreen")
    object UserProfile : Screen("userProfile")
    object UserSettings : Screen("userSettings")
    object LocationForecast : Screen("locationForecast")
    object LocationForecastDetailed : Screen("locationForecastDetailed")
}