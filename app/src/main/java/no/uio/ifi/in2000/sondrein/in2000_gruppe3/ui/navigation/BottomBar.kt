package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun BottomBar(onHomeClick: () -> Unit, onFavoritesClick: () -> Unit, navController: NavHostController) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (navController.currentDestination?.route == "home")
                        MaterialTheme.colorScheme.primary
                    else
                        LocalContentColor.current
                )
            }
            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorites",
                    tint = if (navController.currentDestination?.route == "favorites")
                        MaterialTheme.colorScheme.primary
                    else
                        LocalContentColor.current
                )
            }
        }

    }
}