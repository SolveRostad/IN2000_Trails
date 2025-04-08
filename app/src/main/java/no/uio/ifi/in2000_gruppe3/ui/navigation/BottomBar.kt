package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
    BottomAppBar(
        modifier = Modifier.height(100.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Favorites
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "favorites")
                            navController.navigate(Screen.Favorites.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        modifier = Modifier.size(35.dp),
                        tint = if (navController.currentDestination?.route == "favorites")
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                }

                // Home (logo)
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "home")
                            navController.navigate(Screen.Home.route)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Home",
                        modifier = Modifier.size(70.dp),
                        tint = Color.Unspecified
                    )
                }

                // OpenAI
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "openai")
                            navController.navigate(Screen.OpenAI.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "OpenAI",
                        modifier = Modifier.size(35.dp),
                        tint = if (navController.currentDestination?.route == "openai")
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                }
            }
        }
    }
}
