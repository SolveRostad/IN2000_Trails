package no.uio.ifi.in2000_gruppe3.data.geminiAI.repository

import no.uio.ifi.in2000_gruppe3.data.geminiAI.datasource.GeminiDatasource
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse

class GeminiRepository() {
    private val datasource = GeminiDatasource()

    suspend fun getAnswer(question: String): GeminiResponse {
        return datasource.askQuestion(question)
    }
}