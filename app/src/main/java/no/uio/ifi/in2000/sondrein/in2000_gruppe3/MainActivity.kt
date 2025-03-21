package no.uio.ifi.in2000.sondrein.in2000_gruppe3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation.AppNavHost
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
