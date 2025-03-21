package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.mapbox.search.autocomplete.PlaceAutocomplete
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUIState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val suggestions = remember { mutableStateOf<List<PlaceAutocompleteSuggestion>>(emptyList()) }

    // Create autocomplete client
    val placeAutocomplete = remember {
        PlaceAutocomplete.create()
    }

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            // Search field
            TextField(
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
                    .fillMaxWidth(0.8f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            keyboardController?.hide()
                            suggestions.value = emptyList()
                            // Handle search
                        }
                        true
                    },
                singleLine = true,
                placeholder = { Text("Search for a place") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        // Show suggestions
        if (suggestions.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            ) {
                items(suggestions.value) { suggestion ->
                    Text(
                        text = suggestion.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
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
                                keyboardController?.hide()
                            }
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}
