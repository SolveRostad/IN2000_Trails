package no.uio.ifi.in2000_gruppe3.ui.screens.user.log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature

@Composable
fun LogNotes(
    logScreenViewModel: LogScreenViewModel,
    feature: Feature
) {
    val logScreenUIState by logScreenViewModel.logScreenUIState.collectAsState()

    var noteText by remember { mutableStateOf(logScreenUIState.hikeNotes[feature.properties.fid] ?: "") }
    var isExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(feature.properties.fid, logScreenUIState.hikeNotes[feature.properties.fid]) {
        if (logScreenUIState.hikeNotes[feature.properties.fid] == null) {
            logScreenViewModel.getNotesForHike(feature.properties.fid)
        } else {
            noteText = logScreenUIState.hikeNotes[feature.properties.fid] ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (noteText.isBlank()) "Legg til notater..." else "Endre notat",
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                placeholder = { Text("Hva syntes du om turen...?") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        isExpanded = false
                        if (noteText.isNotBlank()) {
                            logScreenViewModel.addNotesToLog(feature.properties.fid, noteText)
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { isExpanded = false }) {
                    Text(
                        text = "Avbryt",
                        color = Color(0xFF061C40)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        isExpanded = false
                        logScreenViewModel.addNotesToLog(feature.properties.fid, noteText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF061C40))
                ) {
                    Text("Lagre")
                }
            }
        }
    }
}
