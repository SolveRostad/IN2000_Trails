package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
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
                        if (navController.currentDestination?.route != "favorites")
                            navController.navigate(Screen.Favorites.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = if (navController.currentDestination?.route == "favorites") MaterialTheme.colorScheme.primary
                               else LocalContentColor.current
                    )
                }

                // Home (logo)
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "home")
                            navController.navigate(Screen.Home.route)
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .size(70.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Home",
                        modifier = Modifier.fillMaxSize(),
                        tint = Color.Unspecified
                    )
                }

                // User
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "user")
                            navController.navigate(Screen.User.route)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = if (navController.currentDestination?.route == "user") MaterialTheme.colorScheme.primary
                               else LocalContentColor.current
                    )
                }
            }
        }
    }
}
