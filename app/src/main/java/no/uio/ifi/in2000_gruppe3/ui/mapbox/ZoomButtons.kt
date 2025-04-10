package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun MapboxZoomButtons(
    mapboxViewModel: MapboxViewModel
) {
    Column {
        // Zoom in button
        Surface(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                ),
            color = Color.Transparent
        ) {
            IconButton(onClick = { mapboxViewModel.zoomIn() }) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Zoom in",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Zoom out button
        Surface(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(8.dp)
                ),
            color = Color.Transparent
        ) {
            IconButton(onClick = { mapboxViewModel.zoomOut() }) {
                Icon(
                    painter = painterResource(id = R.drawable.minus),
                    contentDescription = "Zoom out",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }
        }
    }
}
