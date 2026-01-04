package com.example.campus360

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.campus360.screens.SettingsScreen
import com.example.campus360.ui.theme.Campus360Theme
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_displaysVersionAndFeedback() {
        composeTestRule.setContent {
            Campus360Theme {
                val navController = rememberNavController()
                SettingsScreen(navController = navController)
            }
        }

        // Verify Version Info
        composeTestRule.onNodeWithText("Version 1.0.0").assertIsDisplayed()
        
        // Verify Feedback Option
        composeTestRule.onNodeWithText("Send Feedback").assertIsDisplayed()
        composeTestRule.onNodeWithText("Share your thoughts with us").assertIsDisplayed()
    }
}
