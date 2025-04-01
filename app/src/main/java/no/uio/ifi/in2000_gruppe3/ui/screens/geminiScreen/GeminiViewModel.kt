package no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse
import no.uio.ifi.in2000_gruppe3.data.geminiAI.repository.GeminiRepository

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
}

data class GeminiUIState(
    val response: String = "",
    val isLoading: Boolean = false
)