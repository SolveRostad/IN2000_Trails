package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Feature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HikeScreen(
    viewModel: HikeScreenViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.hikeScreenUIState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.feature.properties.rutenavn.first()) }, //Må kanskje endres
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Hike Details",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Avstand til turen: ${uiState.feature.properties.distance_meters.toFloat() / 1000.0} km")
                    Text(text = if (uiState.feature.properties.gradering.isEmpty()) "Vanskelighetsgrad: Ukjent" else "Vanskelighetsgrad: ${uiState.feature.properties.gradering.first()}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Route Description",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This is a detailed description of the hiking route. It includes information about the terrain, landmarks, and what to expect along the trail."
                    )
                }
            }
        }
    }
}