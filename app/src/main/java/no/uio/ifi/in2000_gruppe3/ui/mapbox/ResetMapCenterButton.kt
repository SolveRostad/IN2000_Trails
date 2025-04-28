package no.uio.ifi.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.composables.core.Icon
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun ResetMapCenterButton(
    homeScreenViewModel: HomeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    Card(
        modifier = Modifier
            .size(38.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.85f)
        )
    ) {
        IconButton(
            onClick = {
                mapboxViewModel.centerOnUserPosition()
                homeScreenViewModel.fetchForecast(
                    mapboxViewModel.mapboxUIState.value.latestUserPosition!!
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location_arrow_2),
                contentDescription = "Sentrer kart",
                tint = Color.Black.copy(alpha = 0.8f),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
    }
}
