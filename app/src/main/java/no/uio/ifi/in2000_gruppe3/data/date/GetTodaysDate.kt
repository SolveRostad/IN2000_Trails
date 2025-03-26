package no.uio.ifi.in2000_gruppe3.data.date

import java.text.SimpleDateFormat
import java.util.*

fun getTodaysDate(): String {
    val locale = Locale("no", "NO")
    val dateFormat = SimpleDateFormat("EEEE 'Kl' HH:mm", locale)
    return dateFormat.format(Date()).replaceFirstChar { it.uppercaseChar() }
}