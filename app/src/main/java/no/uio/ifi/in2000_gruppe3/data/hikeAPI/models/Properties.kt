package no.uio.ifi.in2000_gruppe3.data.hikeAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val distance_meters: Int,
    val gradering: List<String>,
    val rutenavn: String
)