package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.datasource

import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models.Locationforecast
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

// Get temperature at selected place right now
fun getTemperature(lat: Double, lon: Double): Locationforecast {
    val urlString = "https://api.met.no/weatherapi/locationforecast/2.0/classic?lat=$lat&lon=$lon"

    return try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", "IN2000_TripPlanner/1.0 (sondrein@uio.no)")
        connection.connect()

        val responseCode = connection.responseCode
        val responseText = connection.inputStream.bufferedReader().use { it.readText() }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val json = JSONObject(responseText)
            val temp = json.getJSONObject("properties")
                .getJSONArray("timeseries").getJSONObject(0)
                .getJSONObject("data").getJSONObject("instant")
                .getJSONObject("details").getDouble("air_temperature")
            "$temp°C"
        } else {
            "Wrong connection: ${connection.responseCode}"
        }
    } catch (e: Exception) {
        "Feil ved henting av værdata: ${e.message}"
    }
}