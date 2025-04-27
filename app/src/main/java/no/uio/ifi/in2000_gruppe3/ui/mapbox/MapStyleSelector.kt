package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun MapStyleSelector(
    mapboxViewModel: MapboxViewModel
) {
    var expanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        )
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
