package no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse
import no.uio.ifi.in2000_gruppe3.data.geminiAI.repository.GeminiRepository
import no.uio.ifi.in2000_gruppe3.ui.screens.hikeCardScreen.HikeScreenViewModel
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

class GeminiViewModel : ViewModel() {
    private val geminiRepository = GeminiRepository()

    private val _geminiUIState = MutableStateFlow(GeminiUIState())
    val geminiUIState: StateFlow<GeminiUIState> = _geminiUIState

    fun askQuestion(question: String) {
        viewModelScope.launch {
            _geminiUIState.update {
                it.copy(isLoading = true)
            }
            when (val response = geminiRepository.getAnswer(question)) {
                is GeminiResponse.Success -> {
                    _geminiUIState.update {
                        it.copy(response = response.text, isLoading = false)
                    }
                }
                is GeminiResponse.Error -> {
                    _geminiUIState.update {
                        it.copy(response = "Error: ${response.message}", isLoading = false)
                    }
                }
            }
        }
    }

    fun updateHikeDescription(
        name: String,
        hikeScreenViewModel: HikeScreenViewModel,
        homeScreenViewModel: HomeScreenViewModel
    ) {
        val prompt = "Gi en kort, engasjerende beskrivelse av turen \"$name\" som ligger på koordinatene " +
                "${hikeScreenViewModel.hikeScreenUIState.value.feature.geometry.coordinates}. " +
                "Ikke nevn koordinatene direkte, men bruk stedsnavn og rutenavn hvis mulig. " +
                "Beskriv hva som gjør turen spesiell, og gi anbefalinger basert på værforholdene her: " +
                "${homeScreenViewModel.homeScreenUIState.value.forecast?.properties?.timeseries?.firstOrNull()?.data?.instant?.details?.air_temperature}°C. " +
                "Hvis temperaturen ikke er tilgjengelig, utelat værrelatert informasjon."

        askQuestion(prompt)
    }
}

data class GeminiUIState(
    val response: String = "",
    val isLoading: Boolean = false
)