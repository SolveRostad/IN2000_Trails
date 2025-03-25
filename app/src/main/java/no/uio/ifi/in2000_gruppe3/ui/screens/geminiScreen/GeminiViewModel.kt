package no.uio.ifi.in2000_gruppe3.ui.screens.geminiScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.geminiAI.models.GeminiResponse
import no.uio.ifi.in2000_gruppe3.data.geminiAI.repository.GeminiRepository

class GeminiViewModel: ViewModel() {
    private val geminiRepository = GeminiRepository()

    private val _geminiUIState = MutableStateFlow(GeminiUIState())
    val geminiUIState: StateFlow<GeminiUIState> = _geminiUIState


    fun askQuestion(question: String) {
        viewModelScope.launch {
            try {
                _geminiUIState.update {
                    it.copy(isLoading = true)
                }
                when (val response = geminiRepository.getResponse(question)) {
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
            } catch (e: Exception) {
                Log.e("GeminiViewModel", "Error: ${e.message}")
                _geminiUIState.update {
                    it.copy(
                        isLoading = false,
                        response = "Error: ${e.message}"
                    )
                }
            }
        }
    }
}

data class GeminiUIState(
    val response: String = "",
    val isLoading: Boolean = false
)