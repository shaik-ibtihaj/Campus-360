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
