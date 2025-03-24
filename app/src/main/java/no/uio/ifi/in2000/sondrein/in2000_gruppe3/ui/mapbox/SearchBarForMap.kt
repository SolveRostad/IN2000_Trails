package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(homeScreenViewModel: HomeScreenViewModel) {
    val homeScreenUIState by homeScreenViewModel.homeScreenUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val isSearchActive = homeScreenUIState.searchQuery.isNotEmpty() || homeScreenUIState.searchResponse.isNotEmpty()

    // Full-screen surface when search is active
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (isSearchActive) Color.White else Color.Transparent,
        shadowElevation = if (isSearchActive) 4.dp else 0.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = homeScreenUIState.searchQuery,
                onValueChange = { homeScreenViewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search for location") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (homeScreenUIState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            homeScreenViewModel.updateSearchQuery("")
                            keyboardController?.hide()
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                ),
            )

            if (homeScreenUIState.searchResponse.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(homeScreenUIState.searchResponse) { suggestion ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    homeScreenViewModel.getSelectSearchResultPoint(suggestion)
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
