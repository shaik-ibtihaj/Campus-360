package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.mock.MockData
import com.example.campus360.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(context))
    val recentSearches by viewModel.recentSearches.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Campus360",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Search.route) },
                placeholder = { Text("Search", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = "Search", tint = Color.Gray)
                },
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    disabledBorderColor = Color(0xFFE0E0E0)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    icon = Icons.Outlined.Home,
                    label = "Rooms",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.Search.route) }
                )
                QuickAccessCard(
                    icon = Icons.Outlined.LocationOn,
                    label = "POIs",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.POI.route) }
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAccessCard(
                    icon = Icons.Outlined.FavoriteBorder,
                    label = "Favorites",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.Favorites.route) }
                )
                QuickAccessCard(
                    icon = Icons.Outlined.LocationOn,
                    label = "Map",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Screen.Map.route) }
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Searches",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                if (recentSearches.isNotEmpty()) {
                    TextButton(
                        onClick = { viewModel.clearSearchHistory() },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Clear", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (recentSearches.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No recent searches yet",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            } else {
                recentSearches.forEach { search ->
                    RecentSearchItem(
                        query = search.query,
                        onClick = {
                            viewModel.addSearchQuery(search.query)
                            val room = MockData.rooms.find { it.name.contains(search.query, ignoreCase = true) }
                            if (room != null) {
                                navController.navigate(Screen.RoomDetails.createRoute(room.id))
                            } else {
                                navController.navigate(Screen.Search.route)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun QuickAccessCard(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RecentSearchItem(query: String, onClick: () -> Unit) {
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
                Icons.Outlined.Search,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                query,
                fontSize = 15.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )
        NavigationBarItem(
            icon = { Icon(if (selectedTab == 1) Icons.Filled.LocationOn else Icons.Outlined.LocationOn, contentDescription = "Map") },
            label = { Text("Map") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )
        NavigationBarItem(
            icon = { Icon(if (selectedTab == 2) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, contentDescription = "Favorites") },
            label = { Text("Favorites") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
        NavigationBarItem(
            icon = { Icon(if (selectedTab == 3) Icons.Filled.Settings else Icons.Outlined.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) }
        )
    }
}
