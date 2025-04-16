package no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun EmptyDrawerInfoCard(
    navController: NavHostController
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ai_sparkle_icon),
                contentDescription = "AI Sparkle Icon",
                modifier = Modifier
                    .size(60.dp)
                    .weight(0.2f)
            )

            Text(
                text = "Mine anbefaliner i dag",
                fontSize = 24.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Icon(
                painter = painterResource(R.drawable.ai_sparkle_icon),
                contentDescription = "AI Sparkle Icon",
                modifier = Modifier
                    .size(60.dp)
                    .weight(0.2f)
            )
        }

        Text(
            text = "eller"
        )

        Button(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF57B9FF)),
            onClick = { navController.navigate("chatbot") }
        ) {
            Text(
                text = "Chat med meg"
            )
        }
    }
}