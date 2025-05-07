package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun BottomBar(navController: NavHostController) {
    val currentDestination = navController.currentDestination?.route

    BottomAppBar(
        containerColor = Color.White,
        modifier = Modifier.height(100.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Favorites
                IconButton(
                    onClick = {
                        if (currentDestination != Screen.Favorites.route)
                            navController.navigate(Screen.Favorites.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(30.dp)
                        .background(
                            if (currentDestination == Screen.Favorites.route) Color(0xFFADC3D2)
                            else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        modifier = Modifier.fillMaxSize(),
                        tint = if (currentDestination == Screen.Favorites.route) Color(0xFF061C40)
                               else LocalContentColor.current
                    )
                }

                // Location forecast for user location
                IconButton(
                    onClick = {
                        if (currentDestination != Screen.LocationForecast.route)
                            navController.navigate(Screen.LocationForecast.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .background(
                            if (currentDestination == Screen.LocationForecast.route) Color(0xFFADC3D2)
                            else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.weather),
                        contentDescription = "Location forecast",
                        modifier = Modifier.fillMaxSize(),
                        tint = if (currentDestination == Screen.LocationForecast.route) Color(0xFF061C40)
                               else LocalContentColor.current
                    )
                }

                // Home (logo)
                IconButton(
                    onClick = {
                        if (currentDestination != Screen.Home.route)
                            navController.navigate(Screen.Home.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Home",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Unspecified
                    )
                }

                // Ånund chatbot
                IconButton(
                    onClick = {
                        if (currentDestination != Screen.Chatbot.route)
                            navController.navigate(Screen.Chatbot.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .background(
                            if (currentDestination == Screen.Chatbot.route) Color(0xFFADC3D2)
                            else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.aanund),
                        contentDescription = "Chatbot",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Unspecified
                    )
                }

                // Profile
                IconButton(
                    onClick = {
                        if (currentDestination != Screen.Profile.route)
                            navController.navigate(Screen.Profile.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .size(40.dp)
                        .background(
                            if (
                                currentDestination == Screen.Profile.route ||
                                currentDestination == Screen.ProfileSettings.route ||
                                currentDestination == Screen.ProfileSelect.route
                                ) Color(0xFFADC3D2)
                            else Color.Transparent,
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.trophy),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        tint = if (
                            currentDestination == Screen.Profile.route ||
                            currentDestination == Screen.ProfileSettings.route ||
                            currentDestination == Screen.ProfileSelect.route
                            ) Color(0xFF061C40)
                            else LocalContentColor.current
                    )
                }
            }
        }
    }
}
