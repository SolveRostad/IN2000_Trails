package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.runtime.getValue
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUIState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val suggestions = remember { mutableStateOf<List<PlaceAutocompleteSuggestion>>(emptyList()) }

    // Create autocomplete client
    val placeAutocomplete = remember {
        PlaceAutocomplete.create()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 30.dp),
        contentAlignment = Alignment.Center
    ) {
        // Search field
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { newQuery ->
                viewModel.updateSearchQuery(newQuery)

                // Search for places when query changes
                coroutineScope.launch {
                    if (newQuery.length >= 3) {
                        val response = placeAutocomplete.suggestions(newQuery)

                        if (response.isValue) {
                            suggestions.value = response.value ?: emptyList()
                        } else {
                            Log.e("SearchBar", "Error fetching suggestions", response.error)
                        }
                    } else {
                        suggestions.value = emptyList()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search for a place") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
        )

        // Show suggestions
        if (suggestions.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 70.dp, 16.dp, 0.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                items(suggestions.value) { suggestion ->
                    Text(
                        text = suggestion.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Handle selection
                                coroutineScope.launch {
                                    val result = placeAutocomplete.select(suggestion)
                                    result.onValue { place ->
                                        // Update the map position using the selected place
//                                        viewModel.updateMapPosition(
//                                            place.coordinate?.latitude ?: 0.0,
//                                            place.coordinate?.longitude ?: 0.0
//                                        )
                                    }
                                }
                                // Clear suggestions
                                suggestions.value = emptyList()
                            }
                            .padding(16.dp)
                    )
                    Divider()
                }
            }
        }
    }
}
