package no.uio.ifi.in2000_gruppe3.data.locationForecastAPI

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000_gruppe3.data.locationForecastAPI.repository.LocationForecastRepository

fun main() = runBlocking {
    val repository = LocationForecastRepository()

    val forecast = withContext(Dispatchers.IO) {
        repository.getForecast(59.911491, 10.757933)
    }

    if (forecast != null) {
        println("Test passerte! Mottatt værdata: $forecast")
    } else {
        println("Test feilet! Ingen data mottatt.")
    }
}

