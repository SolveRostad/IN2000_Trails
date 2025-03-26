package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar(
        modifier = Modifier
            .height(100.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column {
            // Linje som skiller bottom bar fra resten av skjermen
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

            // Knapper for å navigere mellom skjermer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "home")
                            navController.navigate(Screen.Home.route)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (navController.currentDestination?.route == "home")
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                }
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "favorites")
                            navController.navigate(Screen.Favorites.route)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        tint = if (navController.currentDestination?.route == "favorites")
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                }
                IconButton(
                    onClick = {
                        if (navController.currentDestination?.route != "gemini")
                            navController.navigate(Screen.Gemini.route)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Gemini",
                        tint = if (navController.currentDestination?.route == "gemini")
                            MaterialTheme.colorScheme.primary
                        else
                            LocalContentColor.current
                    )
                }
            }
        }
    }
}