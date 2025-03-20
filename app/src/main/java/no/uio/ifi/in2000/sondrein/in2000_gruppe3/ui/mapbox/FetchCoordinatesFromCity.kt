package no.uio.ifi.in2000.sondrein.in2000_gruppe3.ui.mapbox

import android.content.Context
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.R
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

suspend fun getCoordinates(city: String, context: Context): Point {
    val apiKey = context.getString(R.string.mapbox_access_token)
    val urlString = "https://api.mapbox.com/geocoding/v5/mapbox.places/$city.json?access_token=$apiKey&country=no"

    return withContext(Dispatchers.IO) {
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"

            val responseCode = connection.responseCode
            val responseText = connection.inputStream.bufferedReader().use { it.readText() }

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val json = JSONObject(responseText)
                val features = json.getJSONArray("features")
                if (features.length() > 0) {
                    val geometry = features.getJSONObject(0).getJSONObject("geometry")
                    val location = geometry.getJSONArray("coordinates")
                    val lon = location.getDouble(0)
                    val lat = location.getDouble(1)
                    return@withContext Point.fromLngLat(lon, lat)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Returnerer Nesodden hvis API kallet feiler
        Point.fromLngLat( 10.661952, 59.846195)
    }
}