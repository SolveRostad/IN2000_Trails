package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.datasource

import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models.Locationforecast
import java.net.HttpURLConnection
import java.net.URL

/**
 * Fetches weather data from the MET API
 * @param lat Latitude of the location
 * @param lon Longitude of the location
 * @return Locationforecast object
 */

fun getLocationForecast(lat: Double, lon: Double): Locationforecast? {
    val urlString = "https://api.met.no/weatherapi/locationforecast/2.0/classic?lat=$lat&lon=$lon"

    return try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("User-Agent", "IN2000_TripPlanner/1.0 (sondrein@uio.no)")
        connection.connect()

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val responseText = connection.inputStream.bufferedReader().use { it.readText() }

            // Deserialize JSON to Location forecast object
            Json { ignoreUnknownKeys = true }.decodeFromString<Locationforecast>(responseText)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}