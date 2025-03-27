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
            Color(0xFF3388FF),    // Bright blue
            Color(0xFF32CD32),    // Lime green
            Color(0xFFFF8C00),    // Dark orange
            Color(0xFFE91E63),    // Pink
            Color(0xFF9C27B0),    // Purple
            Color(0xFF00BCD4),    // Cyan
            Color(0xFFFF5252),    // Red
            Color(0xFF795548),    // Brown
            Color(0xFF607D8B),    // Blue grey
            Color(0xFFFFEB3B)     // Yellow
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
