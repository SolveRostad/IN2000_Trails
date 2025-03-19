package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCard

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
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

@Composable
fun SmallHikeCard(feature: Feature, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = feature.properties.rutenavn.first(), // Må kanskje endres
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            //Text(text = "Type tur: ${feature.type}")
            Text(text = "Avstand til turen: ${feature.properties.distance_meters.toFloat() / 1000.0} km")
            Text(text = if (feature.properties.gradering.isEmpty()) "Ukjent" else "Vanskelighetsgrad: ${feature.properties.gradering.first()}")
        }
    }
}