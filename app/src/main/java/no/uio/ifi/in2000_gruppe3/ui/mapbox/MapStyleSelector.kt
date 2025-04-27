package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun MapStyleSelector(
    mapboxViewModel: MapboxViewModel
) {
    var expanded = remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .size(40.dp)
            .background(
                color = Color.White.copy(alpha = 0.6f),
                shape = RoundedCornerShape(8.dp)
            ),
        color = Color.Transparent
    ) {
        IconButton(
            onClick = { expanded.value = !expanded.value }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.map_style),
                contentDescription = "Bytt kartstil",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        MapStyleDropdown(
            expanded = expanded,
            mapboxViewModel = mapboxViewModel,
            modifier = Modifier
        )
    }
}
