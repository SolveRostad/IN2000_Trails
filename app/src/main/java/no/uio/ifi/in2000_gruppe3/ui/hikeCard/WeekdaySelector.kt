package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.data.date.getOrderedWeekdays
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel

@Composable
fun WeekdaySelector(
    hikeScreenViewModel: HikeScreenViewModel
) {
    val hikeUIState by hikeScreenViewModel.hikeScreenUIState.collectAsState()

    val todaysDay = getTodaysDay()
    val orderedWeekdays = getOrderedWeekdays(todaysDay)

    var expanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (hikeUIState.selectedDay == todaysDay) "I dag" else hikeUIState.selectedDay)

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Velg dag"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = !expanded },
            modifier = Modifier
                .width(80.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            orderedWeekdays.forEach { day ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (day.toString() == todaysDay) "I dag" else day.toString(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        hikeScreenViewModel.updateSelectedDay(day.toString())
                        expanded = !expanded
                    }
                )
            }
        }
    }
}
