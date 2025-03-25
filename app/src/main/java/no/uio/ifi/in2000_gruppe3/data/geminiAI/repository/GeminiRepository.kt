package no.uio.ifi.in2000_gruppe3.data.geminiAI.repository

import no.uio.ifi.in2000_gruppe3.data.geminiAI.datasource.GeminiDatasource
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse

// This class is responsible for handling the data operations of the Gemini AI.
class GeminiRepository() {
    private val datasource by lazy { GeminiDatasource() }

    // This function sends a request to the Gemini AI and returns the response.
    suspend fun getResponse(text: String): GeminiResponse {
        return datasource.askQuestion(text)
    }
}
