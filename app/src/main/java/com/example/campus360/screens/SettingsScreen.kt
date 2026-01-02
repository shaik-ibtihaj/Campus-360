package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus360.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(3) }
    var darkMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    var offlineMaps by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavBar(selectedTab = selectedTab, onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    0 -> navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    1 -> navController.navigate(Screen.Map.route)
                    2 -> navController.navigate(Screen.Favorites.route)
                    3 -> { }
                }
            })
        }
    ) { paddingValues ->
                Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Appearance") {
                SettingsToggleItem(
                    icon = Icons.Filled.Brightness4,
                    title = "Dark Mode",
                    subtitle = "Use dark theme",
                    checked = darkMode,
                    onCheckedChange = { darkMode = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Notifications") {
                SettingsToggleItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Push Notifications",
                    subtitle = "Receive alerts and updates",
                    checked = notifications,
                    onCheckedChange = { notifications = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "Maps") {
                SettingsToggleItem(
                    icon = Icons.Filled.Download,
                    title = "Offline Maps",
                    subtitle = "Download maps for offline use",
                    checked = offlineMaps,
                    onCheckedChange = { offlineMaps = it }
                )
                HorizontalDivider(color = Color(0xFFF0F0F0))
                SettingsClickItem(
                    icon = Icons.Outlined.Delete,
                    title = "Clear Cache",
                    subtitle = "Free up storage space",
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = "About") {
                SettingsClickItem(
                    icon = Icons.Outlined.Info,
                    title = "App Version",
                    subtitle = "1.0.0",
                    onClick = { }
                )
                HorizontalDivider(color = Color(0xFFF0F0F0))
                SettingsClickItem(
                    icon = Icons.Filled.Article,
                    title = "Terms of Service",
                    subtitle = null,
                    onClick = { }
                )
                HorizontalDivider(color = Color(0xFFF0F0F0))
                SettingsClickItem(
                    icon = Icons.Outlined.Lock,
                    title = "Privacy Policy",
                    subtitle = null,
                    onClick = { }
                )
                HorizontalDivider(color = Color(0xFFF0F0F0))
                SettingsClickItem(
                    icon = Icons.Outlined.Send,
                    title = "Send Feedback",
                    subtitle = null,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

