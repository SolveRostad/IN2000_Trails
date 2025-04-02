package no.uio.ifi.in2000_gruppe3.ui.hikeCard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000_gruppe3.data.date.getOrderedWeekdays
import no.uio.ifi.in2000_gruppe3.data.date.getTodaysDay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekdaySelector(
    onDaySelected: (String) -> Unit
) {
    val todaysDay = getTodaysDay()
    val orderedWeekdays = getOrderedWeekdays(todaysDay)

    var expanded by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableStateOf(todaysDay) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = if (selectedDay == todaysDay) "I dag" else selectedDay)
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Velg dag"
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(120.dp)
        ) {
            orderedWeekdays.forEach { day ->
                DropdownMenuItem(
                    text = {
                        Text(if (day.toString() == todaysDay) "I dag" else day.toString())
                    },
                    onClick = {
                        selectedDay = day.toString()
                        expanded = false
                        onDaySelected(selectedDay)
                    }
                )
            }
        }
    }
}