package no.uio.ifi.in2000_gruppe3.data.openAIAPI.datasource

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000_gruppe3.BuildConfig
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.models.ChatCompletionsRequest
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.models.ChatCompletionsResponse
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.models.ChatMessage

class OpenAIDatasource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true }
            )
        }
    }

    private val endpoint = "https://uio-mn-ifi-in2000-swe1.openai.azure.com/"
    private val apiKey = BuildConfig.OPENAI_API_KEY_1
    private val modelName = "gpt-4o"
    private val apiVersion = "2023-05-15"

    suspend fun getCompletionsSamples(prompt: String): ChatCompletionsResponse = withContext(Dispatchers.IO) {
        val chatRequest = ChatCompletionsRequest(
            messages = listOf(
                ChatMessage(
                    role = "user",
                    content = prompt
                )
            )
        )

        val completionsUrl = "$endpoint/openai/deployments/$modelName/chat/completions?api-version=$apiVersion"

        client.post(completionsUrl) {
            contentType(ContentType.Application.Json)
            header("api-key", apiKey)
            setBody(chatRequest)
        }.body()
    }
}
