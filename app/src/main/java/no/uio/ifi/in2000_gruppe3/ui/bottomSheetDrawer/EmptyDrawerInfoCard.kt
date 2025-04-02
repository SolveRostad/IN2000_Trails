package no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000_gruppe3.R

@Composable
fun EmptyDrawerInfoCard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "God dag!",
            fontSize = 30.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Få anbefalinger",
                fontSize = 20.sp,
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ) {
                Image(
                    modifier = Modifier
                        .size(45.dp)
                        .align(Alignment.Center)
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.ai_sparkle_icon),
                    contentDescription = "AI Sparkle Icon",
                )
            }
        }
    }
}