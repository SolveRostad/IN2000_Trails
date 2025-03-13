package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI

import kotlinx.coroutines.Dispatchers
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.datasource.LocationForecastDatasource
import no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.repository.LocationForecastRepository
import kotlinx.coroutines.*

fun main() = runBlocking {
    val repository = LocationForecastRepository(LocationForecastDatasource())

    val forecast = withContext(Dispatchers.IO) {
        repository.getForecast(59.911491, 10.757933)
    }

    if (forecast != null) {
        println("Test passerte! Mottatt værdata: $forecast")
    } else {
        println("Test feilet! Ingen data mottatt.")
    }
}

