package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.repository

import androidx.compose.ui.graphics.Color
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.datasource.TurAPIDatasource
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models.Turer

/**
 * Repository for the TurAPI
 */
class TurAPIRepository {
    private val turAPIDatasource = TurAPIDatasource()
    private var colorIndex = 0


    suspend fun getTurer(lat: Double, lng: Double, limit: Int): Turer {
        val turer = turAPIDatasource.getTurer(lat, lng, limit)
        turer.features.forEach {feature ->
            feature.color = getColor()
        }
        return turer
    }

    private fun getColor(): String {
        val colors = listOf(
            "rgba(51, 136, 255, 1.0)",    // Bright blue
            "rgba(50, 205, 50, 1.0)",     // Lime green
            "rgba(255, 140, 0, 1.0)",     // Dark orange
            "rgba(233, 30, 99, 1.0)",     // Pink
            "rgba(156, 39, 176, 1.0)",    // Purple
            "rgba(0, 188, 212, 1.0)",     // Cyan
            "rgba(255, 82, 82, 1.0)",     // Red
            "rgba(121, 85, 72, 1.0)",     // Brown
            "rgba(96, 125, 139, 1.0)",    // Blue grey
            "rgba(255, 235, 59, 1.0)"     // Yellow
        )
        val color = colors[colorIndex]

        if (colorIndex == colors.size-1) {
            colorIndex = 0
        } else {
            colorIndex++
        }
        return color
    }
}
