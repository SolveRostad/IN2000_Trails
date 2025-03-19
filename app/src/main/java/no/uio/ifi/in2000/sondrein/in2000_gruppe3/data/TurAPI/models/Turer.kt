package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Turer(
    val features: List<Feature>,
    val type: String
)