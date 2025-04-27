package no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.data.openAIAPI.repository.OpenAIRepository
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

class OpenAIViewModel: ViewModel() {
    private val openAIRepository = OpenAIRepository()

    private val _openAIUIState = MutableStateFlow(OpenAIUIState())
    val openAIUIState: StateFlow<OpenAIUIState> = _openAIUIState

    private val _conversationHistory = mutableStateListOf<ChatbotMessage>()
    val conversationHistory: List<ChatbotMessage> get() = _conversationHistory

    init {
        if (_conversationHistory.isEmpty()) {
            addBotMessage("Hei! Jeg er turbotten Ånund 🤖. Hva kan jeg hjelpe deg med?")
        }
    }

    fun addUserMessage(message: String) {
        _conversationHistory.add(ChatbotMessage(message, true))
    }

    fun addBotMessage(message: String) {
        _conversationHistory.add(ChatbotMessage(message, false))
    }

    // Returns a full response from the OpenAI API
    fun getCompletionsSamples(prompt: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            _openAIUIState.update {
                it.copy(isLoading = true)
            }
            try {
                val response = openAIRepository.getCompletionsSamples(prompt)
                val text = response.choices?.first()?.message?.content ?: "No response received"

                _openAIUIState.update {
                    it.copy(response = text)
                }

                onResult(text)
            } catch (e: Exception) {
                val error = "Nå har du trykket så mye at serveren er lei av deg. Gå deg en tur!"
                _openAIUIState.update {
                    it.copy(response = error)
                }

                onResult(error)
            } finally {
                _openAIUIState.update {
                    it.copy(isLoading = false)
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
                val botMessage = ChatbotMessage("", false)
                _conversationHistory.add(botMessage)
                val messageIndex = _conversationHistory.size - 1

                // Collect the streaming responses
                openAIRepository.getCompletionsStream(prompt).collectLatest { chunk ->
                    _openAIUIState.update { currentState ->
                        // Append each new chunk to the existing response
                        currentState.copy(
                            response = currentState.response + chunk,
                            isLoading = false,
                            isStreaming = true
                        )
                    }
                    _conversationHistory[messageIndex] =
                        _conversationHistory[messageIndex].copy(
                            content = _conversationHistory[messageIndex].content + chunk
                        )
                }
            } catch (e: Exception) {
                _openAIUIState.update {
                    it.copy(
                        response = "Nå har du trykket så mye at serveren er lei av deg. Gå deg en tur!",
                        isLoading = false,
                        isStreaming = false
                    )
                }
            } finally {
                _openAIUIState.update {
                    it.copy(isLoading = false, isStreaming = false)
                }
            }
        }
    }

    fun addLimitationToInputMessage(
        input: String,
        homeScreenViewModel: HomeScreenViewModel
    ) {
        var prompt = "Du er turbotten Ånund og er en turguide i en turapp. " +
                "Meldingen nederst er sendt til deg fra en bruker av appen. " +
                "Du skal kun svare på spørsmålet fra brukeren uten å gi noen annen informasjon. " +
                "Du skal ikke gi noen annen informasjon enn det som er nødvendig for å svare på spørsmålet. " +
                "Du skal kun svare på spørsmål som er relatert til turer, friluftsliv og været. " +
                "Hvis spørsmålet ikke er relatert til det så skal du gi en melding hvor du forteller hvem du er som sier at du kun svarer på spørsmål som er relevante. " +
                "Svar på en hyggelig måte. " +
                "Her er medlingen fra bruker: $input"

        if (input.contains("vær") || input.contains("været") || input.contains("værmelding")) {
            prompt += "Her er informasjonene du trenger om været: ${homeScreenViewModel.homeScreenUIState.value.forecast?.properties?.timeseries}"
        }

        getCompletionsStream(prompt)
    }
}

data class OpenAIUIState(
    val response: String = "",
    val isLoading: Boolean = false,
    val isStreaming: Boolean = false
)
