package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(
    homeScreenViewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier
) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isSearchActive =
        homeScreenUIState.searchQuery.isNotEmpty() || homeScreenUIState.searchResponse.isNotEmpty()

    Surface(
        color = if (isSearchActive) Color.White else Color.Transparent,
        shadowElevation = if (isSearchActive) 4.dp else 0.dp,
    ) {
        Column {
            Box(
                modifier = modifier
                    .padding(top = 10.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Search field
                TextField(
                    value = homeScreenUIState.searchQuery,
                    onValueChange = { newQuery ->
                        homeScreenViewModel.updateSearchQuery(newQuery)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                keyboardController?.hide()
                                homeScreenViewModel.updateSearchQuery("")
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
            if (homeScreenUIState.searchResponse.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .padding(horizontal = 10.dp),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(homeScreenUIState.searchResponse) { suggestion ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    homeScreenViewModel.getSelectedSearchResultPoint(suggestion)
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

fun getIconFromString(iconName: String): Int {
    return when (iconName) {
        "marker" -> R.drawable.marker
        "lodging" -> R.drawable.lodging
        "building" -> R.drawable.building
        "information" -> R.drawable.information
        "restaurant" -> R.drawable.restaurant
        "bus" -> R.drawable.bus
        "florist" -> R.drawable.florist
        "cinema" -> R.drawable.cinema
        "fast-food" -> R.drawable.fast_food
        else -> {
            Log.d("UNKNOWN ICON", iconName)
            R.drawable.ic_launcher_background
        }
    }
}
