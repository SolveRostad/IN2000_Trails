package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
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
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

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
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            keyboardController?.hide()
                            viewModel.updateSearchQuery("")
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
        if (uiState.searchResponse.isNotEmpty()) {
            LazyColumn(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background, RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
            ) {
                items(uiState.searchResponse) { suggestion ->
                    Log.d("SearchBarForMap", suggestion.toString())
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                            .clickable {
                                viewModel.updateSearchQuery("")
                                keyboardController?.hide()
                            },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Column {
                            Text(
                                text = suggestion.name,
                                fontSize = 16.sp
                            )
                            if (suggestion.formattedAddress != null) {
                                Text(
                                    text = suggestion.formattedAddress!!,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        Icon(
                            painter = painterResource(id = getIconFromString(suggestion.makiIcon?: "")),
                            contentDescription = "descriptive_icon",
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                    if (suggestion != uiState.searchResponse.last()) {
                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.0.dp
                        )
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
