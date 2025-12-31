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

@Composable
fun Campus360NavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Map.route) {
            MapScreen(navController = navController)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.POI.route) {
            POIScreen(navController = navController)
        }
        composable(Screen.StartEndSelection.route) {
            StartEndSelectionScreen(navController = navController)
        }
        composable(
            route = Screen.RoomDetails.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            RoomDetailsScreen(navController = navController, roomId = roomId)
        }
        composable(
            route = Screen.POIDetails.route,
            arguments = listOf(navArgument("poiId") { type = NavType.StringType })
        ) { backStackEntry ->
            val poiId = backStackEntry.arguments?.getString("poiId") ?: ""
            POIDetailsScreen(navController = navController, poiId = poiId)
        }
        composable(
            route = Screen.Navigation.route,
            arguments = listOf(
                navArgument("from") { type = NavType.StringType },
                navArgument("to") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val from = backStackEntry.arguments?.getString("from") ?: ""
            val to = backStackEntry.arguments?.getString("to") ?: ""
            NavigationScreen(navController = navController, from = from, to = to)
        }
    }
}
