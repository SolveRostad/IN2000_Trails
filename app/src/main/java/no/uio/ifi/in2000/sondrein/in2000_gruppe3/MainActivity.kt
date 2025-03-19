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
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.AppNavHost
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.favoriteScreen.FavoriteScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard.HikeCard
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.home.HomeScreen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.theme.IN2000_gruppe3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IN2000_gruppe3Theme {
                Surface {
                    AppNavHost()
                }
            }
        }
    }
}
