package no.uio.ifi.in2000_gruppe3.data.hikeAPI.repository

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.datasource.HikeAPIDatasource
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.DifficultyInfo
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen.OpenAIViewModel

/**
 * Repository for the Hike API
 */
class HikeAPIRepository(private val openAIViewModel: OpenAIViewModel) {
    private val hikeAPIDatasource = HikeAPIDatasource()
    private var colorIndex = 0
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val generatedNames = mutableMapOf<Int, String>()

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
            feature.difficultyInfo = getDifficultyInfo(feature.properties.gradering ?: "Ukjent")

            if (feature.properties.desc == null || feature.properties.desc!!.contains("_")) {
                val cachedName = generatedNames[feature.properties.fid]
                if (cachedName != null) {
                    feature.properties.desc = cachedName
                } else {
                    generateAndSaveName(feature, openAIViewModel)
                }
            }
        }
        return features
    }

    private fun generateAndSaveName(
        feature: Feature,
        openAIViewModel: OpenAIViewModel
    ) {
        coroutineScope.launch {
            val prompt = "Basert på denne turen \"${feature}, " +
                    "Gi meg et passende navn på turen. " +
                    "Gi meg kun navnet, ikke skriv noe mer. " +
                    "Ikke bruk noen tegn i navnet. " +
                    "Ikke bruk noen form for ID. " +
                    "Bruk mellomrom mellom ord der det er naturlig. " +
                    "Bruk koordinatene for å vite hvor turen går. "

            openAIViewModel.getCompletionsSamples(prompt) { result ->
                val generatedName = result.trim()

                // Update the feature's description
                feature.properties.desc = generatedName

                // Cache the generated name
                generatedNames[feature.properties.fid] = generatedName
            }
        }
    }

    private fun getColor(): Color {
        val colors = listOf(
            Color(0xFF1E90FF),
            Color(0xFF4535C0),
            Color(0xFF0000FF),
            Color(0xFFA205A2),
            Color(0xFFF84E9D),
            Color(0xFFFF1493),
            Color(0xFFFF00FF),
            Color(0xFFFFFF00),
            Color(0xFFFF4500),
            Color(0xFFFF0000),
        )
        val color = colors[colorIndex]

        if (colorIndex == colors.size - 1) {
            colorIndex = 0
        } else {
            colorIndex++
        }
        return color
    }

    private fun getDifficultyInfo(gradering: String): DifficultyInfo {
        return when (gradering) {
            "G" -> DifficultyInfo("ENKEL", Color(0xFF4CAF50)) // Green
            "B" -> DifficultyInfo("MIDDELS", Color(0xFFFFC107)) // Yellow/Amber
            "R" -> DifficultyInfo("KREVENDE", Color(0xFFFF9800)) // Orange
            "S" -> DifficultyInfo("EKSPERT", Color(0xFFF44336)) // Red
            else -> DifficultyInfo("UKJENT", Color(0xFF757575)) // Medium Gray
        }
    }
}
