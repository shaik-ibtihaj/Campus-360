package com.example.campus360

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.campus360.screens.HomeScreen
import com.example.campus360.ui.theme.Campus360Theme
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysTitleAndRecentSearches() {
        composeTestRule.setContent {
            Campus360Theme {
                val navController = rememberNavController()
                HomeScreen(navController = navController)
            }
        }

        // Verify Title
        composeTestRule.onNodeWithText("Campus360").assertIsDisplayed()
        
        // Verify Recent Searches section
        composeTestRule.onNodeWithText("Recent Searches").assertIsDisplayed()
        
        // Verify Quick Access cards
        composeTestRule.onNodeWithText("Rooms").assertIsDisplayed()
        composeTestRule.onNodeWithText("POIs").assertIsDisplayed()
    }
}
