package no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository

import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.datasource.HikeAPIDatasource
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature

/**
 * Repository for the TurAPI
 */
class HikeAPIRepository {
    private val hikeAPIDatasource = HikeAPIDatasource()
    private var colorIndex = 0

    suspend fun getHikes(
        lat: Double,
        lng: Double,
        limit: Int,
        featureType: String,
        minDistance: Int
    ): List<Feature> {
        val features = hikeAPIDatasource.getHikes(lat, lng, limit, featureType, minDistance)
        features.forEach { feature ->
            feature.color = getColor()
        }
        return features
    }

    private fun getColor(): Color {
        val colors = listOf(
            Color(0xFFFF0000),
            Color(0xFFFFA500),
            Color(0xFFFF00FF),
            Color(0xFF0000FF),
            Color(0xFFFFFF00),
            Color(0xFFFF1493),
            Color(0xFFFF4500),
            Color(0xFF800080),
            Color(0xFFFF69B4),
            Color(0xFF1E90FF)
        )
        val color = colors[colorIndex]

        if (colorIndex == colors.size - 1) {
            colorIndex = 0
        } else {
            colorIndex++
        }
        return color
    }
}
