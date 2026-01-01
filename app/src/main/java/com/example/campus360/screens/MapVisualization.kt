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
    
    var zoom by remember { mutableStateOf(1f) }
    var pan by remember { mutableStateOf(Offset.Zero) }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, panDelta, zoomDelta, _ ->
                    zoom = (zoom * zoomDelta).coerceIn(1f, 5f)
                    pan += panDelta
                }
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val scale = min(
            canvasWidth / FLOOR_PLAN_WIDTH,
            canvasHeight / FLOOR_PLAN_HEIGHT
        )

        val scaledWidth = FLOOR_PLAN_WIDTH * scale
        val scaledHeight = FLOOR_PLAN_HEIGHT * scale

        val offsetX = (canvasWidth - scaledWidth) / 2
        val offsetY = (canvasHeight - scaledHeight) / 2

        fun project(x: Float, y: Float): Offset {
            val viewX = x * scale + offsetX
            val viewY = y * scale + offsetY
            return Offset(
                x = viewX * zoom + pan.x,
                y = viewY * zoom + pan.y
            )
        }

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
                if (showGraph) {
            edges.forEach { edge ->
                val fromNode = nodes.find { it.id == edge.fromNodeId }
                val toNode = nodes.find { it.id == edge.toNodeId }

                if (fromNode != null && toNode != null) {
                    drawLine(
                        color = Color.Red,
                        start = project(fromNode.x, fromNode.y),
                        end = project(toNode.x, toNode.y),
                        strokeWidth = 2f
                    )
                }
            }
        }

        if (showGraph) {
            nodes.forEach { node ->
                drawCircle(
                    color = Color.Blue,
                    radius = 5f,
                    center = project(node.x, node.y)
                )
            }
        }

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

