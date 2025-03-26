package no.uio.ifi.in2000_gruppe3.ui.loader

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Loader() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(70.dp)
            .padding(8.dp),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 4.dp
    )
}