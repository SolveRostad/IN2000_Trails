package no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(
    navController: NavHostController
) {
    val openAIViewModel: OpenAIViewModel = viewModel()
    val openAIUIState by openAIViewModel.openAIUIState.collectAsState()
    val conversationHistory = openAIViewModel.conversationHistory

    val coroutineScope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    LaunchedEffect(conversationHistory.size) {
        if (conversationHistory.isNotEmpty()) {
            listState.animateScrollToItem(conversationHistory.size - 1)
        }
        input = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Turbotten Ånund 🤖") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Column {
                ChatInputField(
                    openAIUIState = openAIUIState,
                    value = input,
                    onValueChange = { input = it },
                    onSend = {
                        keyboardController?.hide()
                        openAIViewModel.addUserMessage(input)
                        coroutineScope.launch {
                            openAIViewModel.getCompletionsStream(input)
                        }
                    }
                )
                BottomBar(navController = navController)
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = conversationHistory,
                    key = { message -> "${message.isFromUser}_${message.content.hashCode()}" }
                ) { message ->
                    MessageBubble(message)
                }
            }
        }
    }
}
