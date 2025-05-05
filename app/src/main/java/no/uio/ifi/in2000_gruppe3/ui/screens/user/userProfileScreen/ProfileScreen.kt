package no.uio.ifi.in2000_gruppe3.ui.screens.userProfileScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import no.uio.ifi.in2000_gruppe3.R
import no.uio.ifi.in2000_gruppe3.ui.navigation.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileScreenViewModel: ProfileScreenViewModel,
    navController: NavHostController
) {
    val profileUIState by profileScreenViewModel.ProfileScreenUIState.collectAsState()
    var profile by remember { mutableStateOf("") }
    var expandedProfileId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profiler",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold) }
            )},
        bottomBar = { BottomBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App logo
            Image(
                painter = painterResource(id = R.drawable.logo_slogan),
                contentDescription = "App Logo",
                modifier = Modifier.fillMaxWidth()
                    .size(150.dp)
                    .padding(top = 24.dp, bottom = 16.dp)
            )
            Row (
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ){
                OutlinedTextField(
                    value = profile,
                    onValueChange = { profile = it },
                    placeholder = { Text("Skriv inn ønsket brukernavn") },
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(30.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(30.dp))
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyUp && keyEvent.key == Key.Enter) {
                                if (profile.isNotBlank()) {
                                    Log.d("UserScreen", "Adding profile ${profile}")
                                    profileScreenViewModel.addProfile(profile)
                                    profile = ""
                                }
                            }
                            true
                        },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.weight(0.4f),
                    onClick = {
                        if (profile.isNotBlank()) {
                            Log.d("UserScreen", "Adding profile ${profile}")
                            profileScreenViewModel.addProfile(profile)
                            profile = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF061C40))
                ) {
                    Text(text = "Legg til bruker")
                }
            }

            // Profile list or empty state
            when {
                profileUIState.profiles.isEmpty() -> {
                    Text(
                        text = "Ingen brukere her gitt 🤔",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(profileUIState.profiles) { profile ->
                            val isSelected = profile.username == profileUIState.selectedUser

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = if (isSelected) Color(0xFF061C40) else Color.LightGray,
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            expandedProfileId = if (expandedProfileId == profile.username) null else profile.username
                                            Log.d("ProfileScreen", "Clicked on profile: ${profile.username}")
                                        }
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.AccountCircle,
                                            contentDescription = "User Icon",
                                            modifier = Modifier.size(24.dp),
                                            tint = if (isSelected) Color.White else Color.Black
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = profile.username,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = if (isSelected) Color.White else Color.Black
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = expandedProfileId == profile.username,
                                    onDismissRequest = { expandedProfileId = null },
                                    modifier = Modifier
                                        .fillMaxWidth(0.9f)
                                        .align(Alignment.Center)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Button(
                                            onClick = {
                                                Log.d("UserScreen", "Selected profile: ${profile.username}")
                                                profileScreenViewModel.selectProfile(profile.username)
                                                expandedProfileId = null
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF061C40))
                                        ) {
                                            Text("Velg bruker")
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (profile.username != "defaultUser") {
                                            Button(
                                                onClick = {
                                                    profileScreenViewModel.deleteProfile(profile.username)
                                                    expandedProfileId = null
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                            ) {
                                                Text(text = "Slett bruker")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}