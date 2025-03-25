package no.uio.ifi.in2000_gruppe3.data.turAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)