package com.example.campus360.data.mock

import com.example.campus360.data.model.*
import com.example.campus360.data.NavigationGraph

object NavigationMockData {

    val navigationNodes = listOf(

        // =========================
        // Entrance
        // =========================
        NavigationNode(
            id = "entrance_main",
            name = "Entrance",
            floor = 1,
            x = 4100f,
            y = 1720f,
            type = NodeType.ENTRANCE
        ),

        // =========================
        // Seminar Hall
        // =========================
        NavigationNode(
            id = "seminar_hall",
            name = "Seminar Hall",
            floor = 1,
            x = 1030f,
            y = 1420f,
            type = NodeType.ROOM
        ),

        // =========================
        // Corridor / Connecting Nodes
        // =========================
        NavigationNode("conn_1", "Connecting Node", 1, 1110f, 1240f, NodeType.CORRIDOR),
        NavigationNode("conn_2", "Connecting Node", 1, 1110f, 1110f, NodeType.CORRIDOR),
        NavigationNode("conn_3", "Connecting Node", 1, 1110f, 830f, NodeType.CORRIDOR),
        NavigationNode("conn_4", "Connecting Node", 1, 2310f, 830f, NodeType.CORRIDOR),
        NavigationNode("conn_5", "Connecting Node", 1, 2310f, 1720f, NodeType.CORRIDOR),
        NavigationNode("conn_6", "Connecting Node", 1, 3610f, 1720f, NodeType.CORRIDOR),

        // =========================
        // West Wing Rooms
        // =========================
        NavigationNode("room_1610", "J1610", 1, 1230f, 830f, NodeType.ROOM),
        NavigationNode("room_1620", "J1620", 1, 1600f, 830f, NodeType.ROOM),
        NavigationNode("room_1630", "J1630", 1, 1980f, 830f, NodeType.ROOM),
        NavigationNode("room_1640", "J1640", 1, 2310f, 720f, NodeType.ROOM),
        NavigationNode("room_1650_1660", "J1650 / J1660", 1, 2610f, 720f, NodeType.ROOM),

        // =========================
        // East Wing Rooms
        // =========================
        NavigationNode("room_1670", "J1670", 1, 3000f, 1720f, NodeType.ROOM),
        NavigationNode("room_1680", "J1680", 1, 3530f, 1720f, NodeType.ROOM),

        // =========================
        // Common Areas
        // =========================
        NavigationNode("cafeteria", "Cafeteria", 1, 2740f, 1720f, NodeType.ROOM),
        NavigationNode("workspace", "Workspace", 1, 3610f, 1830f, NodeType.ROOM),

        // =========================
        // Amenities
        // =========================
        NavigationNode("washroom_west", "Washroom", 1, 900f, 1110f, NodeType.RESTROOM),
        NavigationNode("washroom_east", "Washroom", 1, 3860f, 1720f, NodeType.RESTROOM),



    )
