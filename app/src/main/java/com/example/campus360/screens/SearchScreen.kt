package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.MockData
import com.example.campus360.data.Room

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }
    
    val filteredRooms = MockData.rooms.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
        it.building.contains(searchQuery, ignoreCase = true)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Room", color = Color.Gray) },
                        leadingIcon = {
                            Icon(Icons.Outlined.Search, contentDescription = "Search", tint = Color.Gray)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Color.Gray)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF0066FF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
            
            if (searchQuery.isEmpty()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Suggestions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    MockData.rooms.take(4).forEach { room ->
                        SuggestionItem(
                            room = room,
                            onClick = {
                                navController.navigate(Screen.RoomDetails.createRoute(room.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Text(
                        "Filters",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            icon = Icons.Outlined.Layers,
                            label = "Floors",
                            onClick = { showFilters = true }
                        )
                        FilterChip(
                            icon = Icons.Outlined.Apartment,
                            label = "Buildings",
                            onClick = { showFilters = true }
                        )
                        FilterChip(
                            icon = Icons.Outlined.LocationOn,
                            label = "POI Categories",
                            onClick = { showFilters = true }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(filteredRooms) { room ->
                        SuggestionItem(
                            room = room,
                            onClick = {
                                navController.navigate(Screen.RoomDetails.createRoute(room.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionItem(room: Room, onClick: () -> Unit) {
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
            Icon(
                Icons.Outlined.Home,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                room.name,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FilterChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0)),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Black
            )
            Text(
                label,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}
