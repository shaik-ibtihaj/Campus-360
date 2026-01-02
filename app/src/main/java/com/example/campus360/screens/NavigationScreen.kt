package com.example.campus360.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Elevator
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
    
    LaunchedEffect(isNavigating) {
        if (isNavigating && path.isNotEmpty()) {
            currentFloor = path[0].floor
            for (i in path.indices) {
                currentStepIndex = i
                if (i < path.size && path[i].floor != currentFloor) {
                    delay(2000)
                    currentFloor = path[i].floor
                }
                delay(2000)
            }
        } else {
            currentStepIndex = 0
            currentFloor = startNode.floor
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Navigation") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showInstructions = !showInstructions }) {
                        Icon(
                            if (showInstructions) Icons.Filled.LocationOn else Icons.Filled.List,
                            contentDescription = "Toggle View"
                        )
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
        ) {
            // 1. Map section (Canvas)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (!showInstructions) {
                    MapVisualization(
                        floor = currentFloor,
                        nodes = NavigationMockData.navigationNodes,
                        edges = NavigationMockData.navigationEdges,
                        path = path,
                        currentStepIndex = currentStepIndex,
                        showGraph = !isNavigating,
                        modifier = Modifier.fillMaxSize()
                    )

                    FloorMapLegend(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    )

                    // Optional: Floor change indicator as a small overlay on the map
                    if (currentFloor != startNode.floor && currentFloor != endNode.floor) {
                        val floorChangeNode = path.find { it.floor == currentFloor && it.type in listOf(NodeType.STAIRS, NodeType.ELEVATOR) }
                        if (floorChangeNode != null) {
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(16.dp),
                                color = Color(0xFFFF9800),
                                shape = RoundedCornerShape(24.dp),
                                shadowElevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        if (floorChangeNode.type == NodeType.STAIRS) Icons.Filled.MoreVert else Icons.Filled.Elevator,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Floor $currentFloor",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(instructions) { index, instruction ->
                            InstructionCard(
                                instruction = instruction,
                                isActive = isNavigating && index == currentStepIndex,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
