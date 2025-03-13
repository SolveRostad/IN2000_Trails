package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.LocationForecastAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Next12Hours(
    val details: DetailsX? = null,
    val summary: Summary
)