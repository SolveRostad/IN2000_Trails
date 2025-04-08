package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mapbox.maps.Style
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun MapStyleDropdownMenu(
    mapboxViewModel: MapboxViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .size(40.dp)
            .background(
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ),
        color = Color.Transparent
    ) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mapstyle),
                contentDescription = "Bytt kartstil",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(140.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "🌲 Natur",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                onClick = {
                    mapboxViewModel.updateMapStyle(Style.OUTDOORS)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "🛰️ Satellitt",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
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
