package no.uio.ifi.in2000_gruppe3.ui.mapSearchbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel

@Composable
fun SuggestionColumn(mapboxViewModel: MapboxViewModel) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
                        focusManager.clearFocus()

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