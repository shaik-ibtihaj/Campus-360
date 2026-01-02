package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.campus360.data.*
import com.example.campus360.data.model.*
import com.example.campus360.data.mock.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus360.viewmodel.RoutingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(navController: NavController, from: String, to: String) {
    val routingViewModel: RoutingViewModel = viewModel()
    var isNavigating by remember { mutableStateOf(false) }
    var currentFloor by remember { mutableStateOf(1) }
    var currentStepIndex by remember { mutableStateOf(0) }
    var showInstructions by remember { mutableStateOf(false) }
    
    val startNode = remember {
        NavigationMockData.getNavigationNodeForRoom(from)
            ?: NavigationMockData.findNodeByName(from)
            ?: NavigationMockData.navigationNodes.first()
    }
    
    val endNode = remember {
        NavigationMockData.getNavigationNodeForRoom(to)
            ?: NavigationMockData.findNodeByName(to)
            ?: NavigationMockData.navigationNodes.last()
    }
    
    val path = remember {
        NavigationMockData.navigationGraph.findShortestPath(startNode.id, endNode.id) ?: emptyList()
    }
    
    val totalDistance = remember {
        if (path.isNotEmpty()) {
            NavigationMockData.navigationGraph.getTotalDistance(path)
        } else 0f
    }
    
    val instructions = remember {
        if (path.isNotEmpty()) {
            NavigationInstructionGenerator().generateInstructions(path, NavigationMockData.navigationEdges)
        } else emptyList()
    }
