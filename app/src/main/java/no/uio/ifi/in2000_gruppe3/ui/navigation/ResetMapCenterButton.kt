package no.uio.ifi.in2000_gruppe3.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.composables.core.Icon
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

@Composable
fun ResetMapCenterButton(
    mapboxViewModel: MapboxViewModel
) {
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
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_arrow),
                contentDescription = "Sentrer kart",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 4.dp)
            )
        }
    }
}
