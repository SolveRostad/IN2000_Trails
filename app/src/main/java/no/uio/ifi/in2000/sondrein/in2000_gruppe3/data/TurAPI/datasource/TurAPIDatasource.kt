package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Turer

/**
 * Datasource for fetching turdata from the TurDB API
 */
class TurAPIDatasource {

    private val ktorHttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getTurer(lat: Double, lng: Double, limit: Int): Turer {
        val urlString = "http://turdb.info.gf:3000/api/routes/nearby?lat=$lat&lng=$lng&limit=$limit"

        return try {
            ktorHttpClient.get(urlString).body()
        } catch (e: Exception) {
            e.printStackTrace()
            Turer(emptyList(), "Error")
        }
    }
}