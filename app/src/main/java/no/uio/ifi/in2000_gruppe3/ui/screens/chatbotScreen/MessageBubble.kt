package no.uio.ifi.in2000_gruppe3.ui.screens.chatbotScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun MessageBubble(chatbotMessage: ChatbotMessage) {
    val alignment    = if (chatbotMessage.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleColor  = if (chatbotMessage.isFromUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer
    val contentColor = if (chatbotMessage.isFromUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer
    val bubbleShape  = if (chatbotMessage.isFromUser) RoundedCornerShape(16.dp, 4.dp, 16.dp, 16.dp) else RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)
    val paddingEnd   = if (chatbotMessage.isFromUser) 0.dp else 64.dp
    val paddingStart = if (chatbotMessage.isFromUser) 64.dp else 0.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = paddingEnd),
        contentAlignment = alignment
    ) {
        Card(
            shape = bubbleShape,
            colors = CardDefaults.cardColors(
                containerColor = bubbleColor
            )
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                MarkdownText(
                    markdown = chatbotMessage.content,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = contentColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}
