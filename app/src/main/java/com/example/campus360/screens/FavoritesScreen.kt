package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.mock.MockData
import com.example.campus360.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(2) }
    var selectedCategory by remember { mutableIntStateOf(0) }
    val categories = listOf("All", "Rooms", "POIs")
    
    val favoriteRoomIds by viewModel.favoriteRoomIds.collectAsState()
    val favoritePOIIds by viewModel.favoritePOIIds.collectAsState()
    
    val favoriteRooms = MockData.rooms.filter { favoriteRoomIds.contains(it.id) }
    val favoritePOIs = MockData.pointsOfInterest.filter { favoritePOIIds.contains(it.id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorites") },
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
                    2 -> { }
                    3 -> navController.navigate(Screen.Settings.route)
                }
            })
        }
    ) { paddingValues ->         
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    FilterChip(
                        onClick = { selectedCategory = index },
                        label = { Text(category) },
                        selected = selectedCategory == index,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF1976D2),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (selectedCategory == 0 || selectedCategory == 1) {
                    if (favoriteRooms.isNotEmpty()) {
                        item {
                            Text(
                                "Rooms",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(favoriteRooms) { room ->
                            FavoriteRoomCard(
                                name = room.name,
                                location = "${room.building} • Floor ${room.floor}",
                                type = room.type,
                                isFavorite = favoriteRoomIds.contains(room.id),
                                onClick = { navController.navigate(Screen.RoomDetails.createRoute(room.id)) },
                                onToggle = { viewModel.toggleRoomFavorite(room.id) }
                            )
                        }
                    }
                }
                
                if (selectedCategory == 0 || selectedCategory == 2) {
                    if (favoritePOIs.isNotEmpty()) {
                        item {
                            Text(
                                "Points of Interest",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(favoritePOIs) { poi ->
                            FavoriteRoomCard(
                                name = poi.name,
                                location = "${poi.building} • Floor ${poi.floor}",
                                type = poi.type.name.replace("_", " "),
                                isFavorite = favoritePOIIds.contains(poi.id),
                                onClick = { navController.navigate(Screen.POIDetails.createRoute(poi.id)) },
                                onToggle = { viewModel.togglePOIFavorite(poi.id) }
                            )
                        }
                    }
                }


