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

            // 2. Bottom instruction panel
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    if (isNavigating && currentStepIndex < instructions.size) {
                        val currentInstruction = instructions[currentStepIndex]
                        
                        // Current Instruction Detail
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFFE3F2FD), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    when (currentInstruction.type) {
                                        InstructionType.TURN_LEFT -> Icons.Filled.ArrowBack
                                        InstructionType.TURN_RIGHT -> Icons.Filled.ArrowForward
                                        InstructionType.STAIRS_UP, InstructionType.STAIRS_DOWN -> Icons.Filled.MoreVert
                                        InstructionType.ELEVATOR_UP, InstructionType.ELEVATOR_DOWN -> Icons.Filled.Elevator
                                        InstructionType.ARRIVE -> Icons.Filled.Place
                                        else -> Icons.Filled.ArrowUpward
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(28.dp),
                                    tint = Color(0xFF1976D2)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    currentInstruction.description,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                if (currentInstruction.distance > 0) {
                                    Text(
                                        "${currentInstruction.distance.roundToInt()}m remaining",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(color = Color(0xFFEEEEEE))
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Stats and Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(
                                    "${(totalDistance / 75).roundToInt()} min",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1976D2)
                                )
                                Text(
                                    "${totalDistance.roundToInt()}m • Walking",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (!isNavigating) {
                                Button(
                                    onClick = { isNavigating = true },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                                ) {
                                    Icon(Icons.Filled.Navigation, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Start")
                                }
                            } else {
                                OutlinedButton(
                                    onClick = {
                                        isNavigating = false
                                        currentStepIndex = 0
                                        currentFloor = startNode.floor
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Stop")
                                }
                                    Button(
                                        onClick = {
                                            routingViewModel.clearRoutingState()
                                            navController.navigate(Screen.Home.route) {
                                                popUpTo(Screen.Home.route) {
                                                    inclusive = true
                                                }
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                    ) {
                                        Text("Done")
                                    }

                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun InstructionCard(
    instruction: NavigationInstruction,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFF1976D2) else Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
              when (instruction.type) {
                  InstructionType.START -> Icons.Filled.PlayArrow
                  InstructionType.TURN_LEFT -> Icons.Filled.ArrowBack
                  InstructionType.TURN_RIGHT -> Icons.Filled.ArrowForward
                  InstructionType.STRAIGHT -> Icons.Filled.ArrowUpward
                  InstructionType.STAIRS_UP, InstructionType.STAIRS_DOWN -> Icons.Filled.MoreVert
                  InstructionType.ELEVATOR_UP, InstructionType.ELEVATOR_DOWN -> Icons.Filled.Elevator
                  InstructionType.ARRIVE -> Icons.Filled.Place
              },
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isActive) Color.White else Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    instruction.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) Color.White else Color.Black
                )
                if (instruction.distance > 0) {
                    Text(
                        "${instruction.distance.roundToInt()}m",
                        fontSize = 14.sp,
                        color = if (isActive) Color.White.copy(alpha = 0.8f) else Color.Gray
                    )
                }
            }
            
            if (instruction.type in listOf(InstructionType.STAIRS_UP, InstructionType.STAIRS_DOWN, 
                InstructionType.ELEVATOR_UP, InstructionType.ELEVATOR_DOWN)) {
                Box(
                    modifier = Modifier
                        .background(
                            if (isActive) Color.White.copy(alpha = 0.2f) else Color(0xFFFF9800).copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "Floor ${instruction.toNode.floor}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else Color(0xFFFF9800)
                    )
                }
            }
        }
    }
}
