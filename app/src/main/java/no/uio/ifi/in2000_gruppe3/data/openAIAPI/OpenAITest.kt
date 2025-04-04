package no.uio.ifi.in2000_gruppe3.data.openAIAPI

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository

fun main() = runBlocking {
    val repository = OpenAIRepository()

    val prompt = "Gi meg en liste over de 10 mest populære turistmålene i Oslo, Norge."

    val completions = repository.getCompletionsSamples(prompt)

    println(completions.choices?.first()?.message?.content)
}