package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@Composable
fun HikeCard(
    hikeScreenviewModel: HikeScreenViewModel,
    mapboxViewModel: MapboxViewModel
) {
    val uiState by hikeScreenviewModel.hikeScreenUIState.collectAsState()

    Card {
        Column(modifier = Modifier.padding(16.dp)) {

            HikeCardMapPreview(mapboxViewModel, uiState.feature)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rute detaljer",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Avstand til turen: ${uiState.feature.properties.distance_meters.toFloat() / 1000.0} km")
            Text(text = if (uiState.feature.properties.gradering.isEmpty()) "Vanskelighetsgrad: Ukjent" else "Vanskelighetsgrad: ${uiState.feature.properties.gradering.first()}")

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rute beskrivelse",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is a detailed description of the hiking route. It includes information about the terrain, landmarks, and what to expect along the trail."
            )
        }
    }
}