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
