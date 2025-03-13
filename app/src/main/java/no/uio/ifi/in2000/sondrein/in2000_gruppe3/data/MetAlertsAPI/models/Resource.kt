package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Resource(
    val description: String,
    val mimeType: String,
    val uri: String
)