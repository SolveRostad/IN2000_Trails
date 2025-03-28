package no.uio.ifi.in2000_gruppe3.ui.locationForecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun LocationForecastByHour(
    tid: String,
    icon: String,
    temperature: String,
    windSpeed: String,
    humidity: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = tid,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(35.dp)
        )

        val iconURL = getWeatherIconUrl(icon)
        Image(
            painter = rememberAsyncImagePainter(iconURL),
            contentDescription = "Weather-icon",
            modifier = Modifier
                .size(40.dp)
                .weight(0.8f)
        )

        listOf(temperature, windSpeed, humidity).forEach { text ->
            Text(
                text = if (text == temperature) "${temperature}°C" else text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = if (text == temperature) Modifier.weight(1f).padding(start = 4.dp) else Modifier.weight(1f)
            )
        }
    }
}