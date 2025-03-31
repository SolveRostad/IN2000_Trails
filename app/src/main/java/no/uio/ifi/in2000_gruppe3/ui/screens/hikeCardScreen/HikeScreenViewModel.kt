package no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.PropertiesX
import no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen.GeminiViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

class HikeScreenViewModel : ViewModel() {
    private val _hikeScreenUIState = MutableStateFlow<HikeScreenUIState>(
        HikeScreenUIState(
            feature = Feature(
                Geometry(listOf(), "error"),
                properties = PropertiesX(
                    cmt = "error",
                    desc = "error",
                    distance_meters = -1.0,
                    distance_to_point = -1.0,
                    fid = -1,
                    name = "error",
                    src = "error",
                    type = "error",
                    gradering = "error"
                ),
                "error"
            )
        )
    )
    val hikeScreenUIState: StateFlow<HikeScreenUIState> = _hikeScreenUIState.asStateFlow()

    fun updateHike(feature: Feature) {
        _hikeScreenUIState.update {
            it.copy(feature = feature)
        }
    }

    fun updateDate(day: String, date: String, formattedDate: String) {
        _hikeScreenUIState.update {
            it.copy(
                day = day,
                date = date,
                formattedDate = formattedDate
            )
        }
    }

    fun getHikeDescription(
        homeScreenViewModel: HomeScreenViewModel,
        geminiViewModel: GeminiViewModel
    ) {
        val prompt = "Gi en kort, morsom og engasjerende beskrivelse av turen \"${hikeScreenUIState.value.feature.properties.desc}\"" +
                "som ligger på koordinatene ${hikeScreenUIState.value.feature.geometry.coordinates}. " +
                "Ikke nevn koordinatene direkte, men bruk stedsnavn og rutenavn hvis mulig. " +
                "Beskriv hva som gjør turen spesiell, og fortell om kjente steder (hvis det er noen) på turen som kan være interessant. " +
                "Gi også gi anbefalinger til en nordmann om hva man burde ha på seg og om man trenger å ha med seg noe på turen " +
                "basert på værforholdene her (en nordmann bruker ikke lue og votter når det er plussgrader): " +
                "${homeScreenViewModel.homeScreenUIState.value.forecast?.properties?.timeseries?.firstOrNull()?.data?.instant?.details}" +
                "Hvis temperaturen ikke er tilgjengelig, utelat værrelatert informasjon. " +
                "Avslutt med en hyggelig og motiverende melding som 'God tur'. " +
                "Bruk markdown text med * for å gi en bedre brukeroppplevelse. " +
                "Bruk små overskrifter i fet skrift for å dele opp beskrivelsen i de ulike punktene. " +
                "Ikke start med 'Her er en  beskrivelse..' start med tittelen som en liten overskrift og gi meg beskrivelsen. " +
                "Ikke bruk noen paranteser i beskrivelsen som gjentar noe av teksten sendt inn. "

        geminiViewModel.askQuestion(prompt)

        _hikeScreenUIState.update {
            it.copy(description = geminiViewModel.geminiUIState.value.response)
        }
    }
}

data class HikeScreenUIState(
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val feature: Feature,
    val day: String = "",
    val date: String = "",
    val formattedDate: String = "",
    val description: String = ""
)