package com.example.campus360.data.model

data class Building(
    val id: String,
    val name: String,
    val floors: Int,
    val description: String
)

data class RecentSearch(
    val query: String,
    val timestamp: Long
)

data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    INFO,
    ALERT,
    UPDATE
}
