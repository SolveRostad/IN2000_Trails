package no.uio.ifi.in2000_gruppe3.data.geminiAI.models

sealed class GeminiResponse {
    data class Success(val text: String) : GeminiResponse()
    data class Error(val message: String) : GeminiResponse()
}
