package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun MapStyleDropdownMenu(
    viewModel: HomeScreenViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 35.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Column {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = if (viewModel.mapIsDarkmode || viewModel.mapStyle == "SATELLITE") Color.White else Color.Unspecified
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = { Text("Lyst") }, onClick = {
                    viewModel.mapStyle = "STANDARD"
                    viewModel.mapIsDarkmode = false
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Mørkt") }, onClick = {
                    viewModel.updateMapStyle("STANDARD", false)
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Satellitt") }, onClick = {
                    viewModel.updateMapStyle("STANDARD", false)
                    expanded = false
                })
                DropdownMenuItem(text = { Text("Natur") }, onClick = {
                    viewModel.updateMapStyle("STANDARD", false)
                    expanded = false
                })
            }
        }
    }
}
