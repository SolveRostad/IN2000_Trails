package no.uio.ifi.in2000_gruppe3

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import no.uio.ifi.in2000_gruppe3.ui.screens.homeScreen.HomeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class TestAIButton {
    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun testAIButton() {
        composeTestRule.setContent {
            HomeScreen(
                homeScreenViewModel = viewModel(),
                favoritesViewModel = viewModel(),
                mapboxViewModel = viewModel(),
                openAIViewModel = viewModel(),
                hikeViewModel = viewModel(),
                navController = rememberNavController()
            )
        }

        composeTestRule.onNodeWithText("AI icon").isDisplayed()
    }

}