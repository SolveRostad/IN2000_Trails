package no.uio.ifi.in2000_gruppe3.data.date

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

fun getTodaysDayAndTime(): String {
    val locale = Locale("no", "NO")
    val dateFormat = SimpleDateFormat("EEEE 'Kl' HH:mm", locale)
    return dateFormat.format(Date()).replaceFirstChar { it.uppercaseChar() }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTodaysDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.format(Date.from(Instant.now())).split("T").first()
}