package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class MetAlerts(
    val features: List<Feature>,
    val lang: String,
    val lastChange: String,
    val type: String
)