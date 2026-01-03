package com.example.campus360.data.model

data class NavigationNode(
    val id: String,
    val name: String,
    val floor: Int,
    val x: Float,
    val y: Float,
    val type: NodeType = NodeType.CORRIDOR
)

enum class NodeType {
    ROOM,
    CORRIDOR,
    STAIRS,
    ELEVATOR,
    ENTRANCE,
    RESTROOM,
    POI
}

data class NavigationEdge(
    val fromNodeId: String,
    val toNodeId: String,
    val distance: Float
)
