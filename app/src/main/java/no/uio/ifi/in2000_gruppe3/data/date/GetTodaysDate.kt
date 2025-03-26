package no.uio.ifi.in2000_gruppe3.data.date

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
// Returnerer dato i formatet "YYYY-MM-DD"
fun getTodaysDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date())
}

// Returnerer på format 27. mars
fun getDateFormatted(date: String): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formatet på datoen som blir sendt inn
    val outputFormatter = SimpleDateFormat("dd. MMMM", Locale("no", "NO")) // Formatet vi ønsker ut

    // Parsing input datoen
    val parsedDate = inputFormatter.parse(date)

    // Returnerer formatert dato
    return outputFormatter.format(parsedDate)
}

// Returnerer klokkeslett i formatet "HH:mm:ss"
fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

// Returns todays day
@RequiresApi(Build.VERSION_CODES.O)
fun getTodaysDay(): String {
    val dayOfWeek = LocalDate.now().dayOfWeek
    val daysMap = mapOf(
        DayOfWeek.MONDAY to "Mandag",
        DayOfWeek.TUESDAY to "Tirsdag",
        DayOfWeek.WEDNESDAY to "Onsdag",
        DayOfWeek.THURSDAY to "Torsdag",
        DayOfWeek.FRIDAY to "Fredag",
        DayOfWeek.SATURDAY to "Lørdag",
        DayOfWeek.SUNDAY to "Søndag"
    )
    return daysMap[dayOfWeek] ?: "Ukjent dag"
}