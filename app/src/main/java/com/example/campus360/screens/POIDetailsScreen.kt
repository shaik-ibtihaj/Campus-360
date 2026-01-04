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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = color
                )
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        poi.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "${poi.building} • Floor ${poi.floor}",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Category,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            poi.type.name.replace("_", " "),
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        poi.description,
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        lineHeight = 24.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { navController.navigate(Screen.Navigation.createRoute("current", poi.name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Filled.Navigation, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Get Directions", fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
