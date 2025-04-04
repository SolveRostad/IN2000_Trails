package no.uio.ifi.in2000_gruppe3.ui.screens.openAIScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository

class OpenAIViewModel: ViewModel() {
    private val openAIRepository = OpenAIRepository()

    private val _openAIUIState = MutableStateFlow(OpenAIUIState())
    val openAIUIState: StateFlow<OpenAIUIState> = _openAIUIState

    fun getCompletionsSamples(prompt: String) {
        viewModelScope.launch {
            _openAIUIState.update {
                it.copy(isLoading = true)
            }
            try {
                _openAIUIState.update {
                    val response = openAIRepository.getCompletionsSamples(prompt)
                    val text = response.choices?.first()?.message?.content
                    it.copy(response = text ?: "No response received", isLoading = false)
                }
            } catch (e: Exception) {
                _openAIUIState.update {
                    it.copy(response = "Error: ${e.message}", isLoading = false)
                }
            }
        }
    }
}

data class OpenAIUIState(
    val response: String? = "",
    val isLoading: Boolean = false
)