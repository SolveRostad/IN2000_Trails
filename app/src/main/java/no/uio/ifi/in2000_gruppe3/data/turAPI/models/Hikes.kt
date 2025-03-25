package no.uio.ifi.in2000_gruppe3.data.turAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Hikes(
    val features: List<Feature>,
    val type: String
)