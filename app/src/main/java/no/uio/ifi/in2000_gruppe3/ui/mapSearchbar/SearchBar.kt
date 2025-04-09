package no.uio.ifi.in2000_gruppe3.ui.mapSearchbar

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun SearchBarForMap(
    mapboxViewModel: MapboxViewModel,
    homeScreenViewModel: HomeScreenViewModel,
) {
    val mapboxUIState by mapboxViewModel.mapboxUIState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

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
                homeScreenViewModel.setSheetState(SheetDrawerDetent.SEMIPEEK)
                if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                    mapboxViewModel.updateSearchQuery("")
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
                true
            },
        singleLine = true,
        placeholder = { Text("Hvor vil du gå tur?") },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        },
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