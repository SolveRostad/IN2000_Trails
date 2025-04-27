package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mapbox.maps.Style
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun MapStyleSelector(
    mapboxViewModel: MapboxViewModel
) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val mapStyle = mapboxUIState.mapStyle
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(48.dp)  // Samme størrelse som ResetMapCenterButton
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            IconButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.map_style),
                    contentDescription = "Bytt kartstil",
                    tint = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(140.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                // Natur-stil
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "🌲 Natur",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        mapboxViewModel.updateMapStyle(Style.OUTDOORS)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (mapStyle == Style.OUTDOORS)
                            MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                )

                // Satellitt-stil
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "🛰️ Satellitt",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        mapboxViewModel.updateMapStyle(Style.STANDARD_SATELLITE)
                        expanded = false
                    },
                    modifier = Modifier.background(
                        if (mapStyle == Style.STANDARD_SATELLITE)
                            MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                )
            }
        }
    }
}
