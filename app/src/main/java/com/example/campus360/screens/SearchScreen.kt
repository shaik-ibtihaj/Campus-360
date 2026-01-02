package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.campus360.Screen
import com.example.campus360.data.mock.MockData
import com.example.campus360.data.model.Room
import com.example.campus360.viewmodel.HomeViewModel
import com.example.campus360.viewmodel.RoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(context))
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Rooms", "Labs", "Study")
    
    val filteredRooms = MockData.rooms.filter { room ->
        val matchesQuery = room.name.contains(searchQuery, ignoreCase = true) ||
                room.building.contains(searchQuery, ignoreCase = true) ||
                room.type.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Rooms" -> room.type.contains("Lecture", ignoreCase = true) || room.type.contains("Seminar", ignoreCase = true)
            "Labs" -> room.type.contains("Lab", ignoreCase = true)
            "Study" -> room.type.contains("Study", ignoreCase = true)
            else -> true
        }
        matchesQuery && matchesFilter
    }
