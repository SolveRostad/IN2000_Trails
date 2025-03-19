package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Geometry(
    val coordinates: List<List<List<Double>>>,
    val type: String
)