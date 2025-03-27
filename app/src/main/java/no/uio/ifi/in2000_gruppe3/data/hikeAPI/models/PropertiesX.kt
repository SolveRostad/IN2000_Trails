package no.uio.ifi.in2000_gruppe3.data.hikeAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class PropertiesX(
    val cmt: String?,
    val desc: String?,
    val distance_meters: Double,
    val distance_to_point: Double,
    val fid: Int,
    val name: String,
    val src: String,
    val type: String,
    val gradering: String = "sondre sigma",
)