package com.example.campus360.screens

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus360.data.model.*
import kotlinx.coroutines.delay

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.example.campus360.R
import kotlin.math.min

const val FLOOR_PLAN_WIDTH = 4339f
const val FLOOR_PLAN_HEIGHT = 2180f

@Composable
fun IndoorMapCanvas(
    nodes: List<NavigationNode>,
    edges: List<NavigationEdge>,
    path: List<NavigationNode> = emptyList(),
    markerPosition: Offset? = null,
    pulseScale: Float = 1f,
    showGraph: Boolean = true,
    modifier: Modifier = Modifier
) {
    val blueprint = ImageBitmap.imageResource(id = R.drawable.j_block_floor1)
    
    // Zoom and pan state
    var zoom by remember { mutableStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, panDelta, zoomDelta, _ ->
                    // 1. Update zoom with clamping
                    zoom = (zoom * zoomDelta).coerceIn(1f, 5f)
                    
                    // 2. Update pan
                    // Panning is applied in screen space to allow the user to move 
                    // the view naturally regardless of the zoom level.
                    pan += panDelta
                }
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Existing world-to-viewport scaling logic
        // This ensures the blueprint fits the screen while maintaining aspect ratio
        val scale = min(
            canvasWidth / FLOOR_PLAN_WIDTH,
            canvasHeight / FLOOR_PLAN_HEIGHT
        )

        val scaledWidth = FLOOR_PLAN_WIDTH * scale
        val scaledHeight = FLOOR_PLAN_HEIGHT * scale

        // Base offset to center the map on the canvas before user transformation
        val offsetX = (canvasWidth - scaledWidth) / 2
        val offsetY = (canvasHeight - scaledHeight) / 2

        // Transformation helper: World -> Viewport (Base Scale) -> Camera (Zoom/Pan)
        // Zoom is applied after world scaling to maintain perfect alignment between 
        // the background image and the navigation graph.
        // Pan is added last to shift the final viewport into the desired position.
        fun project(x: Float, y: Float): Offset {
            val viewX = x * scale + offsetX
            val viewY = y * scale + offsetY
            return Offset(
                x = viewX * zoom + pan.x,
                y = viewY * zoom + pan.y
            )
        }

        // 1. Draw the blueprint first
        // We calculate its position and size by applying the same camera transforms
        drawImage(
            image = blueprint,
            dstOffset = IntOffset(
                (offsetX * zoom + pan.x).toInt(),
                (offsetY * zoom + pan.y).toInt()
            ),
            dstSize = IntSize(
                (scaledWidth * zoom).toInt(),
                (scaledHeight * zoom).toInt()
            )
        )

        // 2. Draw navigation edges
        if (showGraph) {
            edges.forEach { edge ->
                val fromNode = nodes.find { it.id == edge.fromNodeId }
                val toNode = nodes.find { it.id == edge.toNodeId }

                if (fromNode != null && toNode != null) {
                    drawLine(
                        color = Color.Red,
                        start = project(fromNode.x, fromNode.y),
                        end = project(toNode.x, toNode.y),
                        strokeWidth = 2f // Keep stroke width consistent for readability
                    )
                }
            }
        }

        // 3. Draw navigation nodes
        if (showGraph) {
            nodes.forEach { node ->
                drawCircle(
                    color = Color.Blue,
                    radius = 5f, // Keep radius consistent (do not over-scale)
                    center = project(node.x, node.y)
                )
            }
        }

        // Draw active path if present
        if (path.size >= 2) {
            val pathLine = Path()
            val start = project(path[0].x, path[0].y)
            pathLine.moveTo(start.x, start.y)
            
            for (i in 1 until path.size) {
                val next = project(path[i].x, path[i].y)
                pathLine.lineTo(next.x, next.y)
            }
            
            drawPath(
                path = pathLine,
                color = Color.Blue,
                style = Stroke(
                    width = 6f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                )
            )
        }

        // Draw animated marker if present
        markerPosition?.let { pos ->
            val screenPos = project(pos.x, pos.y)
            drawCircle(
                color = Color(0xFF4CAF50).copy(alpha = 0.3f),
                radius = 20f * pulseScale,
                center = screenPos
            )
            drawCircle(
                color = Color(0xFF4CAF50),
                radius = 10f,
                center = screenPos
            )
        }
    }
}

@Composable
fun MapVisualization(
    floor: Int,
    nodes: List<NavigationNode>,
    edges: List<NavigationEdge>,
    path: List<NavigationNode>,
    currentStepIndex: Int,
    showGraph: Boolean = true,
    modifier: Modifier = Modifier
) {
    val nodesOnFloor = nodes.filter { it.floor == floor }
    val edgesOnFloor = edges.filter { edge ->
        val fromNode = nodes.find { it.id == edge.fromNodeId }
        val toNode = nodes.find { it.id == edge.toNodeId }
        fromNode?.floor == floor && toNode?.floor == floor
    }
    val pathOnFloor = path.filter { it.floor == floor }
    
    var animatedMarkerPosition by remember { mutableStateOf<Offset?>(null) }
    var animationProgress by remember { mutableStateOf(0f) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "marker_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    LaunchedEffect(currentStepIndex, pathOnFloor) {
        if (pathOnFloor.isNotEmpty() && currentStepIndex < pathOnFloor.size) {
            animationProgress = 0f
            
            if (currentStepIndex == 0) {
                animatedMarkerPosition = Offset(pathOnFloor[0].x, pathOnFloor[0].y)
            } else if (currentStepIndex < pathOnFloor.size) {
                val start = pathOnFloor[currentStepIndex - 1]
                val end = pathOnFloor[currentStepIndex]
                
                for (i in 0..100) {
                    val progress = i / 100f
                    animationProgress = progress
                    animatedMarkerPosition = Offset(
                        start.x + (end.x - start.x) * progress,
                        start.y + (end.y - start.y) * progress
                    )
                    delay(20)
                }
            }
        }
    }
    
    Box(modifier = modifier) {
        IndoorMapCanvas(
            nodes = nodesOnFloor,
            edges = edgesOnFloor,
            path = pathOnFloor,
            markerPosition = animatedMarkerPosition,
            pulseScale = pulseScale,
            showGraph = showGraph
        )
        
        Text(
            text = "Floor $floor",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9E9E9E),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
    }
}

@Composable
fun FloorMapLegend(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.9f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        Text(
//            "Legend",
//            fontSize = 12.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.Black
//        )
//
//        LegendItem(Color(0xFF4CAF50), "Start")
//        LegendItem(Color(0xFFF44336), "Destination")
//        LegendItem(Color(0xFF1976D2), "Path")
//        LegendItem(Color(0xFFFF9800), "Stairs")
//        LegendItem(Color(0xFF9C27B0), "Elevator")
//        LegendItem(Color(0xFF2196F3), "Room")
//        LegendItem(Color(0xFF00BCD4), "Restroom")
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Text(
            label,
            fontSize = 10.sp,
            color = Color(0xFF616161)
        )
    }
}
