package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.TurAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String,
    var color: String = "rgba(0, 0, 0, 1.0)"
)