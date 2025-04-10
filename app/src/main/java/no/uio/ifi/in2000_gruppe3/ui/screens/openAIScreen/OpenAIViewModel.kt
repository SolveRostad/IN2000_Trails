package no.uio.ifi.in2000_gruppe3.ui.screens.openAIScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository

class OpenAIViewModel: ViewModel() {
    private val openAIRepository = OpenAIRepository()

    private val _openAIUIState = MutableStateFlow(OpenAIUIState())
    val openAIUIState: StateFlow<OpenAIUIState> = _openAIUIState

    // Returns a full response from the OpenAI API
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

    // Returns a streaming response from the OpenAI API
    fun getCompletionsStream(prompt: String) {
        viewModelScope.launch {
            _openAIUIState.update {
                it.copy(isLoading = true, response = "")
            }

            try {
                _openAIUIState.update {
                    it.copy(isLoading = false, isStreaming = true)
                }

                // Collect the streaming responses
                openAIRepository.getCompletionsStream(prompt).collectLatest { chunk ->
                    _openAIUIState.update { currentState ->
                        // Append each new chunk to the existing response
                        currentState.copy(
                            response = currentState.response + chunk,
                            isStreaming = true
                        )
                    }
                }

                _openAIUIState.update { currentState ->
                    currentState.copy(isStreaming = false)
                }
            } catch (e: Exception) {
                _openAIUIState.update {
                    it.copy(
                        response = "Error: ${e.message}",
                        isLoading = false,
                        isStreaming = false
                    )
                }
            }
        }
    }
}

data class OpenAIUIState(
    val response: String = "",
    val isLoading: Boolean = false,
    val isStreaming: Boolean = false
)
