package no.uio.ifi.in2000_gruppe3.data.geminiAI.datasource

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse

class GeminiDatasource {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-pro-exp-03-25",
        apiKey = BuildConfig.GEMINI_API_KEY,
//        safetySettings = listOf(
//            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.LOW_AND_ABOVE),
//            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
//            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
//            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
//        ),
//        generationConfig = generationConfig {
//            temperature = 1f
//            topK = 64
//            topP = 0.95f
//            maxOutputTokens = 65536
//            responseMimeType = "text/plain"
//        }
    )

    suspend fun askQuestion(question: String): GeminiResponse {
        return try {
            val response: GenerateContentResponse = withContext(Dispatchers.IO) {
                generativeModel.generateContent(question)
            }

            val textPart = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()
            if (textPart is TextPart) {
                GeminiResponse.Success(textPart.text)
            } else {
                GeminiResponse.Error("Response was not in expected text format")
            }
        } catch (e: Exception) {
            GeminiResponse.Error("Nå er jeg utslitt, vent ett lite minutt \n ${e.message}")
        }
    }
}