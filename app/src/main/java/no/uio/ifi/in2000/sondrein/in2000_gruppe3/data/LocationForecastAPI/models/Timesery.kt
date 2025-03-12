package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Timesery(
    val `data`: Data,
    val time: String
)