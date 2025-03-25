package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun MapStyleDropdownMenu(
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val mapIsDarkmode = uiState.mapIsDarkmode

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.padding(5.dp, 30.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Column {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = if (mapIsDarkmode) Color.White else Color.Unspecified
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(80.dp)
            ) {
                DropdownMenuItem(
                    text = { Text("Natur", modifier = Modifier.width(100.dp), textAlign = TextAlign.Center) },
                    onClick = {
                        homeScreenViewModel.updateMapStyle("OUTDOORS", false)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Satellitt", modifier = Modifier.width(100.dp), textAlign = TextAlign.Center) },
                    onClick = {
                        homeScreenViewModel.updateMapStyle("SATELLITE", true)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Dag", modifier = Modifier.width(100.dp), textAlign = TextAlign.Center) },
                    onClick = {
                        homeScreenViewModel.updateMapStyle("STANDARD", false)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Natt", modifier = Modifier.width(100.dp), textAlign = TextAlign.Center) },
                    onClick = {
                        homeScreenViewModel.updateMapStyle("STANDARD", true)
                        expanded = false
                    }
                )
            }
        }
    }
}
