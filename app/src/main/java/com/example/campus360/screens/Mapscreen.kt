package com.example.campus360.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.mock.MockData

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.campus360.data.BlueprintRegistry
import com.example.campus360.data.MapBuilding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(navController: NavController) {
    var currentBuilding by remember { mutableStateOf(MapBuilding.HBLOCK) }
    var currentFloor by remember { mutableIntStateOf(1) }
    var selectedTab by remember { mutableIntStateOf(1) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        
        offset += offsetChange
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campus Map") },
                actions = {
                    IconButton(onClick = { 
                        scale = 1f
                        offset = Offset.Zero
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset View")
                    }
                },
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
                    1 -> { }
                    2 -> navController.navigate(Screen.Favorites.route)
                    3 -> navController.navigate(Screen.Settings.route)
                }
            })
        }
    ) { paddingValues ->
