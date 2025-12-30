package com.example.campus360.data.model

data class PointOfInterest(
    val id: String,
    val name: String,
    val type: POIType,
    val building: String,
    val floor: Int,
    val description: String
)

enum class POIType {
    RESTROOM,
}
