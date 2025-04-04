package no.uio.ifi.in2000_gruppe3.data.openAIAPI

import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository

fun main() {
    val repository = OpenAIRepository()
    val prompt = "Hvem er lennard denby"
    val completions = repository.openAIDatasource.getCompletionsSamples(prompt)
    println(completions?.id)
    println(completions?.choices?.first()?.message?.content)
}