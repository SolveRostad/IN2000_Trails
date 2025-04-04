package no.uio.ifi.in2000_gruppe3.data.openAIAPI.datasource

import com.azure.ai.openai.OpenAIClient
import com.azure.ai.openai.OpenAIClientBuilder
import com.azure.ai.openai.models.ChatCompletions
import com.azure.ai.openai.models.ChatCompletionsOptions
import com.azure.ai.openai.models.ChatMessage
import com.azure.ai.openai.models.ChatRole
import com.azure.core.credential.AzureKeyCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000_gruppe3.BuildConfig

class OpenAIDatasource {
    private val client: OpenAIClient = OpenAIClientBuilder()
        .endpoint("https://uio-mn-ifi-in2000-swe1.openai.azure.com/")
        .credential(AzureKeyCredential(BuildConfig.OPENAI_API_KEY_1))
        .buildClient()

    private val modelID = "gpt-4o"

    suspend fun getCompletionsSamples(prompt: String): ChatCompletions {
        return withContext(Dispatchers.IO) {
            val chatMessages = listOf(ChatMessage(ChatRole.USER, prompt))
            client.getChatCompletions(modelID, ChatCompletionsOptions(chatMessages))
        }
    }
}