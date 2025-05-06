package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        ),
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { expanded.value = !expanded.value }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.map_style),
            contentDescription = "Bytt kartstil",
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .offset(y = 2.dp)
        )

        MapStyleDropdown(
            expanded = expanded,
            mapboxViewModel = mapboxViewModel,
            modifier = Modifier
        )
    }
}
