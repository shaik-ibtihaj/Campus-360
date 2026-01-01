package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun POIDetailsScreen(navController: NavController, poiId: String) {
    val poi = MockData.pointsOfInterest.find { it.id == poiId }
    var isFavorite by remember { mutableStateOf(MockData.favoritePOIIds.contains(poiId)) }
    
    if (poi == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("POI not found")
        }
        return
    }
    
    val (icon, color) = getPOIIconAndColor(poi.type)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(poi.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isFavorite = !isFavorite
                        if (isFavorite) {
                            MockData.favoritePOIIds.add(poiId)
                        } else {
                            MockData.favoritePOIIds.remove(poiId)
                        }
                    }) {
                        Icon(
                            if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color(0xFFE91E63) else Color.Gray
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Share, contentDescription = "Share")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
