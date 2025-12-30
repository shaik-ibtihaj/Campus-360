package com.example.campus360.data.model

data class Room(
    val id: String,
    val name: String,
    val building: String,
    val floor: Int,
    val type: String,
    val capacity: Int,
    val amenities: List<String>,
)

data class ScheduleSlot(
    val time: String,
    val event: String,
    val isAvailable: Boolean
)
