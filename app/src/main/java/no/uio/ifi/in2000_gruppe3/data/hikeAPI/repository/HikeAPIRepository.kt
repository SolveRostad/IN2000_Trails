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
            Color(0xFFFF4500),    // Vermilion
            Color(0xFF008000),    // Forest Green
            Color(0xFFFF6347),    // Tomato Red
            Color(0xFF4169E1),    // Royal Blue
            Color(0xFFDA70D6),    // Orchid
            Color(0xFF2E8B57),    // Sea Green
            Color(0xFFFF8C69),    // Salmon
            Color(0xFF8A2BE2),    // Blue Violet
            Color(0xFFD2691E),    // Chocolate
            Color(0xFF20B2AA)     // Light Sea Green
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
