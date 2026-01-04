package com.example.campus360.data

import com.example.campus360.data.model.*
import java.util.PriorityQueue
import kotlin.math.atan2
import kotlin.math.sqrt

class NavigationGraph(
    private val nodes: List<NavigationNode>,
    private val edges: List<NavigationEdge>
) {
    private val adjacencyMap: Map<String, List<Pair<String, Float>>> = buildAdjacencyMap()

    private fun buildAdjacencyMap(): Map<String, List<Pair<String, Float>>> {
        val map = mutableMapOf<String, MutableList<Pair<String, Float>>>()
        
        edges.forEach { edge ->
            map.getOrPut(edge.fromNodeId) { mutableListOf() }
                .add(Pair(edge.toNodeId, edge.distance))
            map.getOrPut(edge.toNodeId) { mutableListOf() }
                .add(Pair(edge.fromNodeId, edge.distance))
        }
        
        return map
    }

    fun findShortestPath(startNodeId: String, endNodeId: String): List<NavigationNode>? {
        val distances = mutableMapOf<String, Float>()
        val previous = mutableMapOf<String, String?>()
        val visited = mutableSetOf<String>()
        val queue = PriorityQueue<Pair<String, Float>>(compareBy { it.second })

        nodes.forEach { node ->
            distances[node.id] = Float.MAX_VALUE
            previous[node.id] = null
        }
        distances[startNodeId] = 0f
        queue.add(Pair(startNodeId, 0f))

        while (queue.isNotEmpty()) {
            val (currentId, currentDist) = queue.poll()

            if (currentId in visited) continue
            visited.add(currentId)

            if (currentId == endNodeId) break

            adjacencyMap[currentId]?.forEach { (neighborId, edgeWeight) ->
                if (neighborId !in visited) {
                    val newDist = currentDist + edgeWeight
                    if (newDist < distances[neighborId]!!) {
                        distances[neighborId] = newDist
                        previous[neighborId] = currentId
                        queue.add(Pair(neighborId, newDist))
                    }
                }
            }
        }

        if (previous[endNodeId] == null && startNodeId != endNodeId) return null

        val path = mutableListOf<String>()
        var current: String? = endNodeId
        while (current != null) {
            path.add(0, current)
            current = previous[current]
        }

        return path.mapNotNull { nodeId -> nodes.find { it.id == nodeId } }
    }

    fun getNode(nodeId: String): NavigationNode? = nodes.find { it.id == nodeId }

    fun getTotalDistance(path: List<NavigationNode>): Float {
        var total = 0f
        for (i in 0 until path.size - 1) {
            val edge = edges.find { 
                (it.fromNodeId == path[i].id && it.toNodeId == path[i + 1].id) ||
                (it.toNodeId == path[i].id && it.fromNodeId == path[i + 1].id)
            }
            total += edge?.distance ?: 0f
        }
        return total
    }
}

data class NavigationInstruction(
    val type: InstructionType,
    val description: String,
    val distance: Float,
    val fromNode: NavigationNode,
    val toNode: NavigationNode,
    val icon: String
)

enum class InstructionType {
    START,
    STRAIGHT,
    TURN_LEFT,
    TURN_RIGHT,
    STAIRS_UP,
    STAIRS_DOWN,
    ELEVATOR_UP,
    ELEVATOR_DOWN,
    ARRIVE
}

class NavigationInstructionGenerator {
    
    fun generateInstructions(path: List<NavigationNode>, edges: List<NavigationEdge>): List<NavigationInstruction> {
        if (path.size < 2) return emptyList()
        
        val instructions = mutableListOf<NavigationInstruction>()
        
        instructions.add(NavigationInstruction(
            type = InstructionType.START,
            description = "Start at ${path[0].name}",
            distance = 0f,
            fromNode = path[0],
            toNode = path[0],
            icon = "start"
        ))
        
        for (i in 1 until path.size) {
            val current = path[i - 1]
            val next = path[i]
            val distance = getDistance(current, next, edges)
            
            when {
                next.type == NodeType.STAIRS && next.floor > current.floor -> {
                    instructions.add(NavigationInstruction(
                        type = InstructionType.STAIRS_UP,
                        description = "Take stairs up to Floor ${next.floor}",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = "stairs_up"
                    ))
                }
                next.type == NodeType.STAIRS && next.floor < current.floor -> {
                    instructions.add(NavigationInstruction(
                        type = InstructionType.STAIRS_DOWN,
                        description = "Take stairs down to Floor ${next.floor}",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = "stairs_down"
                    ))
                }
                next.type == NodeType.ELEVATOR && next.floor > current.floor -> {
                    instructions.add(NavigationInstruction(
                        type = InstructionType.ELEVATOR_UP,
                        description = "Take elevator up to Floor ${next.floor}",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = "elevator_up"
                    ))
                }
                next.type == NodeType.ELEVATOR && next.floor < current.floor -> {
                    instructions.add(NavigationInstruction(
                        type = InstructionType.ELEVATOR_DOWN,
                        description = "Take elevator down to Floor ${next.floor}",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = "elevator_down"
                    ))
                }
                i < path.size - 1 -> {
                    val direction = calculateDirection(current, next, path.getOrNull(i + 1))
                    val (type, desc) = when (direction) {
                        Direction.LEFT -> Pair(InstructionType.TURN_LEFT, "Turn left")
                        Direction.RIGHT -> Pair(InstructionType.TURN_RIGHT, "Turn right")
                        else -> Pair(InstructionType.STRAIGHT, "Walk straight")
                    }
                    
                    instructions.add(NavigationInstruction(
                        type = type,
                        description = "$desc for ${distance.toInt()}m",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = type.name.lowercase()
                    ))
                }
                else -> {
                    instructions.add(NavigationInstruction(
                        type = InstructionType.STRAIGHT,
                        description = "Walk ${distance.toInt()}m to ${next.name}",
                        distance = distance,
                        fromNode = current,
                        toNode = next,
                        icon = "straight"
                    ))
                }
            }
        }
        
        instructions.add(NavigationInstruction(
            type = InstructionType.ARRIVE,
            description = "Arrive at ${path.last().name}",
            distance = 0f,
            fromNode = path.last(),
            toNode = path.last(),
            icon = "arrive"
        ))
        
        return instructions
    }
    
    private fun getDistance(from: NavigationNode, to: NavigationNode, edges: List<NavigationEdge>): Float {
        val edge = edges.find { 
            (it.fromNodeId == from.id && it.toNodeId == to.id) ||
            (it.toNodeId == from.id && it.fromNodeId == to.id)
        }
        return edge?.distance ?: calculateEuclideanDistance(from, to)
    }
    
    private fun calculateEuclideanDistance(from: NavigationNode, to: NavigationNode): Float {
        val dx = to.x - from.x
        val dy = to.y - from.y
        return sqrt(dx * dx + dy * dy)
    }
    
    private fun calculateDirection(prev: NavigationNode, current: NavigationNode, next: NavigationNode?): Direction {
        if (next == null) return Direction.STRAIGHT
        
        val angle1 = atan2((current.y - prev.y).toDouble(), (current.x - prev.x).toDouble())
        val angle2 = atan2((next.y - current.y).toDouble(), (next.x - current.x).toDouble())
        
        var diff = Math.toDegrees(angle2 - angle1)
        if (diff < -180) diff += 360
        if (diff > 180) diff -= 360
        
        return when {
            diff < -30 -> Direction.LEFT
            diff > 30 -> Direction.RIGHT
            else -> Direction.STRAIGHT
        }
    }
    
    enum class Direction {
        LEFT, RIGHT, STRAIGHT
    }
}
