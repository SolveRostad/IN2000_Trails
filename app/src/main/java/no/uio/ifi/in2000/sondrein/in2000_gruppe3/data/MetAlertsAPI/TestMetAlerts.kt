package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.repository.MetAlertsRepository

fun main() = runBlocking {
    val repository = MetAlertsRepository()

    val alerts = withContext(Dispatchers.IO) {
        repository.getAlerts()
    }

    if (alerts != null) {
        println("Test passerte! Mottatt værdata: $alerts")
    } else {
        println("Test feilet! Ingen data mottatt.")
    }
}