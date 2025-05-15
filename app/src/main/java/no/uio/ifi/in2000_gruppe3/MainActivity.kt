package no.uio.ifi.in2000_gruppe3

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import no.uio.ifi.in2000_gruppe3.ui.navigation.AppNavHost
import no.uio.ifi.in2000_gruppe3.ui.theme.IN2000_gruppe3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
