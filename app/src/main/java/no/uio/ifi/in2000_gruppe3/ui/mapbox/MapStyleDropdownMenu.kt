package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mapbox.maps.Style

@Composable
fun MapStyleDropdownMenu(
    mapboxViewModel: MapboxViewModel,
    modifier: Modifier = Modifier
) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = if (mapboxUIState.mapStyle == Style.OUTDOORS) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(80.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Natur",
                        modifier = Modifier.width(100.dp),
                        textAlign = TextAlign.Center
                    )
                },
                onClick = {
                    mapboxViewModel.updateMapStyle(Style.OUTDOORS)
                    expanded = false
                },
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Satellitt",
                        modifier = Modifier.width(100.dp),
                        textAlign = TextAlign.Center
                    )
                },
                onClick = {
                    mapboxViewModel.updateMapStyle(Style.STANDARD_SATELLITE)
                    expanded = false
                }
            )
        }
    }
}
