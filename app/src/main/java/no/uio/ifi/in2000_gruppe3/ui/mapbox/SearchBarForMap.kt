package no.uio.ifi.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun SearchBarForMap(
    mapboxViewModel: MapboxViewModel,
    modifier: Modifier = Modifier
) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isSearchActive =
        mapboxUIState.searchQuery.isNotEmpty() || mapboxUIState.searchResponse.isNotEmpty()

    Surface(
        color = if (isSearchActive) Color.White else Color.Transparent,
        shadowElevation = if (isSearchActive) 4.dp else 0.dp,
        modifier = modifier.padding(top = 10.dp)
    ) {
        Column {
            Box(
                modifier = modifier.fillMaxWidth(0.8f),
                contentAlignment = Alignment.TopCenter
            ) {
                // Search field
                TextField(
                    value = mapboxUIState.searchQuery,
                    onValueChange = { newQuery ->
                        mapboxViewModel.updateSearchQuery(newQuery)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                keyboardController?.hide()
                                mapboxViewModel.updateSearchQuery("")
                            }
                            true
                        },
                    singleLine = true,
                    placeholder = { Text("Hvor vil du gå tur?") },
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
            if (mapboxUIState.searchResponse.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(horizontal = 10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(mapboxUIState.searchResponse) { suggestion ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    mapboxViewModel.getSelectedSearchResultPoint(suggestion)
                                    keyboardController?.hide()
                                }
                                .padding(16.dp)
                        ) {
                            Text(
                                text = suggestion.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            suggestion.formattedAddress?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }
    }
}
