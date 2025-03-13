package no.uio.ifi.in2000.sondrein.in2000_gruppe3.data.MetAlertsAPI.models

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val altitude_above_sea_level: Int,
    val area: String,
    val awarenessResponse: String,
    val awarenessSeriousness: String,
    val awareness_level: String,
    val awareness_type: String,
    val ceiling_above_sea_level: Int,
    val certainty: String,
    val consequences: String,
    val contact: String,
    val county: List<String>,
    val description: String,
    val event: String,
    val eventAwarenessName: String,
    val eventEndingTime: String? = null,
    val geographicDomain: String,
    val id: String,
    val instruction: String,
    val municipality: List<String>? = null,
    val resources: List<Resource>,
    val riskMatrixColor: String,
    val severity: String,
    val status: String,
    val title: String,
    val triggerLevel: String? = null,
    val type: String,
    val web: String
)