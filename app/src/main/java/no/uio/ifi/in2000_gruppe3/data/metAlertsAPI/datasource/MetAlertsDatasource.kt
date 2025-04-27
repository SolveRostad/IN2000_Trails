package no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.datasource

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000_gruppe3.data.metAlertsAPI.models.MetAlerts
import java.net.HttpURLConnection
import java.net.URL

/**
 * Datasource for fetching MET alerts from the MET API
 */
class MetAlertsDatasource {
    /**
     * Fetches MET alerts from the MET API
     * @return METAlerts object or null if request fails
     */
    suspend fun getMetAlerts(): MetAlerts? {
        return withContext(Dispatchers.IO) {
            val urlString = "https://api.met.no/weatherapi/metalerts/2.0/current.json"

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "IN2000_trails/1.0 (sondrein@uio.no)")
            Log.d("MetAlertsDatasource", "Før connect")
            connection.connect()
            Log.d("MetAlertsDatasource", "Etter connect")

            val responseCode = connection.responseCode
            Log.d("MetAlertsDatasource", "HTTP Response Code: $responseCode")

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                Log.d("MetAlertsDatasource", "API Response: $response")

                val json = Json {
                    ignoreUnknownKeys = true  // Ignorerer ukjente felter
                    isLenient = true }

                // Serialize the response to a MetAlerts object
                json.decodeFromString<MetAlerts>(response)
            } else {
                Log.e("MetAlertsDatasource", "Feil: HTTP $responseCode")
                null
            }
        } catch (e: Exception) {
            Log.e("MetAlertsDatasource", "Feil ved parsing av JSON: ", e)
            null
        }
    }
    }
}
