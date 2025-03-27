package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

@Composable
fun SmallHikeCard(
    mapboxViewModel: MapboxViewModel,
    feature: Feature,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clipToBounds()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = feature.properties.desc ?: "Ukjent rutenavn",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Avstand til turen: ${String.format("%.2f", feature.properties.distance_meters.toFloat() / 1000.0)} km")
            Text(text = if (feature.properties.gradering.isEmpty()) "Vanskelighetsgrad: Ukjent" else "Vanskelighetsgrad: ${feature.properties.gradering.first()}")

            Spacer(modifier = Modifier.height(3.dp))

            HikeCardMapPreview(mapboxViewModel, feature)
        }
    }
}