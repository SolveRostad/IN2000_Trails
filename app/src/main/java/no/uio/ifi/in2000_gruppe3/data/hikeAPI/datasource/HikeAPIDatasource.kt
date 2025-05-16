package no.uio.ifi.in2000_gruppe3.data.hikeAPI.datasource

import android.util.Log
import com.mapbox.geojson.Point
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Hikes

/**
 * Datasource for fetching data from Hike API
 */
class HikeAPIDatasource {

    private val ktorHttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    suspend fun getHikes(
        lat: Double,
        lng: Double,
        limit: Int,
        featureType: String,
        minDistance: Int
    ): List<Feature> {
        val urlString =
            "http://turdb.info.gf:3000/api/routes/nearby?lat=$lat&lng=$lng&limit=$limit&featureType=$featureType&minDistance=$minDistance"

        return try {
            val hikes = ktorHttpClient.get(urlString).body<Hikes>()
            return hikes.features
        } catch (e: Exception) {
            Log.e("HikeAPIDatasource", "getHikes: ${e.message}")
            emptyList()
        }
    }

    suspend fun getHikesById(hikeIds: List<String>, position: Point): List<Feature> {
        val hikeIdsString = hikeIds.joinToString(",")
        val urlString = "http://turdb.info.gf:3000/api/routes/withids?lat=${position.latitude()}&lng=${position.longitude()}&id=$hikeIdsString"

        return try {
            val hikes = ktorHttpClient.get(urlString).body<Hikes>()
            return hikes.features
        } catch (e: Exception) {
            Log.e("HikeAPIDatasource", "getHikesById: ${e.message}")
            emptyList()
        }
    }
}