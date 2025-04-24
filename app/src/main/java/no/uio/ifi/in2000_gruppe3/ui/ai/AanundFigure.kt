package no.uio.ifi.in2000_gruppe3.ui.ai

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.composables.core.Icon
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.bottomSheetDrawer.SheetDrawerDetent
import no.uio.ifi.in2000_gruppe3.ui.mapbox.MapboxViewModel
import no.uio.ifi.in2000_gruppe3.ui.navigation.Screen
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreenViewModel

@Composable
fun AanundFigure(
    homeScreenViewModel: HomeScreenViewModel,
    mapBoxViewModel: MapboxViewModel,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        homeScreenViewModel.setSheetState(SheetDrawerDetent.SEMIPEEK)

        // Clear hikes from map to get AI recommendations
        homeScreenViewModel.clearHikes()
        mapBoxViewModel.clearPolylineAnnotations()
        mapBoxViewModel.updatePointerCoordinates(null)

        Dialog(
            onDismissRequest = { showDialog = false },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDialog = false
                        navController.navigate(Screen.Chatbot.route)
                    }
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Hei, mitt navn er Ånund!\n" +
                                "Jeg er her for å hjelpe deg med å planlegge turer i Oslo/Akershus.\n\n" +
                                "Bruk søkefeltet for å finne turer i et bestemt område, eller trykk på kartet for å oppdage nye turmuligheter.\n\n" +
                                "Hvis du trenger inspirasjon, kan du trykke på meg! Jeg vil gi deg mine beste anbefalinger for de fineste turene å gå akkurat i dag.",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.aanund),
                    contentDescription = "AI icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.TopStart)
                        .offset(x = (-60).dp, y = (-60).dp)
                )

                IconButton(
                    onClick = { showDialog = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    } else {
        Surface(
            modifier = Modifier
                .size(150.dp)
                .offset(x = (-15).dp),
            color = Color.Transparent
        ) {
            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.aanund_white),
                    contentDescription = "AI icon white",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(120.dp) // must be 30dp smaller than the surface
                )
            }
        }
    }
}
