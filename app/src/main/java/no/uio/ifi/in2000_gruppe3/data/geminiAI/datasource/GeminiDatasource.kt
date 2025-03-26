package no.uio.ifi.in2000_gruppe3.data.geminiAI.datasource

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.TextPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse

class GeminiDatasource {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.0-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
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
            Log.e("GeminiDatasource", "Error: ${e.message}")
            GeminiResponse.Error("Error: ${e.message}")
        }
    }
}