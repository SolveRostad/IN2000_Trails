package no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository

import com.azure.ai.openai.models.ChatCompletions
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.datasource.OpenAIDatasource

class OpenAIRepository {
    val openAIDatasource = OpenAIDatasource()

    fun getCompletionsSamples(prompt: String): ChatCompletions? {
        return openAIDatasource.getCompletionsSamples(prompt)
    }
}