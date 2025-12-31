package com.example.campus360

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.campus360.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Search : Screen("search")
    object Map : Screen("map")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object Notifications : Screen("notifications")
    object POI : Screen("poi")
    object RoomDetails : Screen("room/{roomId}") {
        fun createRoute(roomId: String) = "room/$roomId"
    }
    object POIDetails : Screen("poi/{poiId}") {
        fun createRoute(poiId: String) = "poi/$poiId"
    }
    object StartEndSelection : Screen("routing_selection")
    object Navigation : Screen("navigation/{from}/{to}") {
        fun createRoute(from: String, to: String) = "navigation/$from/$to"
    }
}
