package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.mock.MockData
import com.example.campus360.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun POIScreen(navController: NavController) {
    var selectedFilter by remember { mutableStateOf<POIType?>(null) }
    
    val filteredPOIs = if (selectedFilter == null) {
        MockData.pointsOfInterest
    } else {
        MockData.pointsOfInterest.filter { it.type == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Points of Interest") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            onClick = { selectedFilter = null },
                            label = { Text("All") },
                            selected = selectedFilter == null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            onClick = { selectedFilter = POIType.RESTROOM },
                            label = { Text("Restroom") },
                            selected = selectedFilter == POIType.RESTROOM,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF1976D2),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPOIs) { poi ->
                    POICard(
                        poi = poi,
                        onClick = { navController.navigate(Screen.POIDetails.createRoute(poi.id)) }
                    )
                }
            }
        }
    }
}

@Composable
fun POICard(poi: PointOfInterest, onClick: () -> Unit) {
    val (icon, color) = getPOIIconAndColor(poi.type)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    poi.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${poi.building} • Floor ${poi.floor}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    poi.description,
                    fontSize = 13.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

fun getPOIIconAndColor(type: POIType): Pair<ImageVector, Color> {
    return when (type) {
        POIType.RESTROOM -> Pair(Icons.Outlined.AccountBox, Color(0xFF2196F3))
    }
}
