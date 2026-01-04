package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.campus360.viewmodel.RoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartEndSelectionScreen(navController: NavController) {
    val routingViewModel: RoutingViewModel = viewModel()
    val startLocation by routingViewModel.startLocation.collectAsState()
    val destinationLocation by routingViewModel.destinationLocation.collectAsState()

    var activeField by remember { mutableStateOf<SelectionField>(SelectionField.START) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredRooms = MockData.rooms.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.building.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan Your Route") },
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
            // Selection Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SelectionBox(
                        label = "Starting Point",
                        value = startLocation ?: "Select origin",
                        isSelected = activeField == SelectionField.START,
                        onClick = { activeField = SelectionField.START }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SelectionBox(
                        label = "Destination",
                        value = destinationLocation ?: "Select destination",
                        isSelected = activeField == SelectionField.DESTINATION,
                        onClick = { activeField = SelectionField.DESTINATION }
                    )
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Search rooms or buildings...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Results List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredRooms) { room ->
                    ListItem(
                        headlineContent = { Text(room.name, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text("${room.building} • Floor ${room.floor}") },
                        leadingContent = {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF1976D2)
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                if (activeField == SelectionField.START) {
                                    routingViewModel.setStartLocation(room.name)
                                    activeField = SelectionField.DESTINATION
                                } else {
                                    routingViewModel.setDestinationLocation(room.name)
                                }
                                searchQuery = ""
                            }
                            .background(Color.White, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            // Action Button
            if (startLocation != null && destinationLocation != null) {
                Button(
                    onClick = {
                        navController.navigate(Screen.Navigation.createRoute(startLocation!!, destinationLocation!!))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Go Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SelectionBox(
    label: String,
    value: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF8F9FA),
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = if (value != "Select origin" && value != "Select destination") FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color(0xFF1976D2) else Color.Black
        )
    }
}

enum class SelectionField {
    START, DESTINATION
}
