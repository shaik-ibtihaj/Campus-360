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
val buildings = listOf(
        Building("j_block", "J Block", 5, "Applied Science and Technology Building"),
        Building("b1", "Hall Building", 12, "Main academic building"),
        Building("b2", "EV Building", 17, "Engineering and Visual Arts")
    )

    val recentSearches = listOf(
        RecentSearch("J1610", System.currentTimeMillis() - 3600000),
        RecentSearch("Cafeteria", System.currentTimeMillis() - 7200000),
        RecentSearch("Seminar Hall", System.currentTimeMillis() - 86400000)
    )

    val notifications = listOf(
        Notification(
            id = "n1",
            title = "Room Change Alert",
            message = "Seminar moved to Seminar Hall",
            timestamp = "2 hours ago",
            isRead = false,
            type = NotificationType.ALERT
        ),
        Notification(
            id = "n2",
            title = "Map Update",
            message = "New floor plans available for J Block",
            timestamp = "1 day ago",
            isRead = true,
            type = NotificationType.UPDATE
        )
    )
    
    val favoriteRoomIds = mutableListOf("j1610", "j1630", "j1650")
    val favoritePOIIds = mutableListOf("poi_washrooms")
}
