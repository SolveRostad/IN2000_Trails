package no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Feature
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.Geometry
import no.uio.ifi.in2000_gruppe3.data.hikeAPI.models.PropertiesX
import no.uio.ifi.in2000_gruppe3.ui.screens.openAIScreen.OpenAIViewModel
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
        openAIViewModel: OpenAIViewModel,
        selectedDay: String,
        selectedDate: String,
    ) {
        viewModelScope.launch {
            val prompt = "Du er en turguide i en turapp. " +
                    "Gi meg en kort beskrivelse av turen med navnet \"${hikeScreenUIState.value.feature.properties.desc}\". " +
                    "Hvis rutenavnet er ukjent så finn et passende rutenavn. " +
                    "Turen ligger på koordinatene ${hikeScreenUIState.value.feature.geometry.coordinates}, så sørg for å gi informasjon om riktig tur. " +
                    "Du skal IKKE nevne koordinatene, men finne hvilket sted som ligger på koordinatene for så å bruke stedsnavnet. " +
                    "Fortell om hva som gjør turen spesiell og om det er noen kjente steder på turen. " +
                    "Det skal kun være ett kort avsnitt. " +
                    "I tillegg skal du skrive et kort avsnitt om temperaturen for dagen og datoen basert på værforholdene sendt inn. " +
                    "I avsnittet skal du komme med anbefalinger om hvordan man burde kle seg for turen og hva man burde ha med i sekken. " +
                    "Ta i betraktning at de som bruker appen er nordmenn og er vandt til kalde temperaturer, altså bruker man ikke lue og votter når det er 5 grader ute, men ikke nevn det i beskrivelsen. " +
                    "I tillegg skal du skrive et kort avsnitt som inneholder en anbefaling av hvilken dag, utover i dag, man burde gå på tur basert på værforholdet de neste 7 dagene. " +
                    "Du skal altså skrive tre korte avsnitt på formen: \n[Navn på tur]\nInnhold første avnitt med info om turen. \n[Informasjon om været]\nInnhold andre avsnitt om temperatur. \n[Når burde du gå tur?]\nInnhold tredje avsnitt om når det er best vær. " +
                    "Bruk små overskrifter med fet skrifttype og markdown tekst. " +
                    "Du skal IKKE svare som en chatbot, men kun gi meg informasjonen jeg har spurt om. " +
                    "Avslutt med en hyggelig og motiverende melding og en emoji i fet skrift som for eksempel 'God tur!'. " +
                    "Den valgte dag- og datoen er \"$selectedDay\", \"$selectedDate\". " +
                    "All informasjonen du trenger om været er dette: \"${homeScreenViewModel.homeScreenUIState.value.forecast?.properties?.timeseries}\". "

            openAIViewModel.getCompletionsStream(prompt)
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
    val description: String? = ""
)