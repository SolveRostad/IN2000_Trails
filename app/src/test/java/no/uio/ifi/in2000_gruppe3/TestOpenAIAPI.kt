package no.uio.ifi.in2000_gruppe3

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository

fun main() = runBlocking {
    val repository = OpenAIRepository()

    val prompt = "What is the weather like in Oslo?"

    val completions = repository.getCompletionsSamples(prompt)

    println("Completions: $completions")
}