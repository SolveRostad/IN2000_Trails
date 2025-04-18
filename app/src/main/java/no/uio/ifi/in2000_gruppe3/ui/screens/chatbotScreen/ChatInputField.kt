package no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun ChatInputField(
    openAIUIState: OpenAIUIState,
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val readyToSend = value.isNotBlank() && !openAIUIState.isStreaming

    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Skriv en melding...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .onKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                            if (readyToSend) { onSend() }
                        }
                        true
                    },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { if (readyToSend) { onSend() } }
                ),
                maxLines = 4
            )

            IconButton(
                onClick = { if (readyToSend) { onSend() } },
                enabled = readyToSend
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = when {
                        openAIUIState.isStreaming -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        value.isNotBlank() -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    }
                )
            }
        }
    }
}
