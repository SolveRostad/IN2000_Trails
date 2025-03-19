package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val distance_meters: Int,
    val gradering: List<String>,
    val rutenavn: List<String>,
    val rutenummer: String
)