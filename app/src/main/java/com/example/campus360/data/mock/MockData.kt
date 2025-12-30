package com.example.campus360.data.mock

import com.example.campus360.data.model.*

object MockData {
    val rooms = listOf(

        Room(
            id = "j1610",
            name = "J1610",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 40,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1620",
            name = "J1620",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 40,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1630",
            name = "J1630",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 40,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1640",
            name = "J1640",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 45,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1650",
            name = "J1650",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 45,
            amenities = listOf("Projector", "Whiteboard")
        ),
        Room(
            id = "j1660",
            name = "J1660",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 50,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1670",
            name = "J1670",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 40,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "j1680",
            name = "J1680",
            building = "J Block",
            floor = 1,
            type = "Classroom",
            capacity = 40,
            amenities = listOf("Projector", "Whiteboard")
        ),

        Room(
            id = "seminar_hall",
            name = "Seminar Hall",
            building = "J Block",
            floor = 1,
            type = "Seminar Hall",
            capacity = 120,
            amenities = listOf("Projector", "Microphone", "Stage")
        ),
    )

    val pointsOfInterest = listOf(
        PointOfInterest(
            id = "poi_washrooms",
            name = "Washrooms",
            type = POIType.RESTROOM,
            building = "J Block",
            floor = 1,
            description = "Gender-neutral washrooms"
        ),
//        PointOfInterest(
//            id = "cafeteria",
//            name = "Cafeteria",
//            building = "J Block",
//            type = POIType.FOOD,
//            floor = 1,
//            description = ""
//        )
    )
