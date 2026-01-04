package com.example.campus360.data

import com.example.campus360.R

enum class MapBuilding(val displayName: String, val maxFloors: Int) {
    HBLOCK("H Block", 2),
    JBLOCK("J Block", 3)
}

object BlueprintRegistry {
    fun getBlueprint(building: MapBuilding, floor: Int): Int {
        return when (building) {
            MapBuilding.HBLOCK -> {
                when (floor) {
                    1 -> R.drawable.h_floor_1
                    3 -> R.drawable.h_floor_3
                    else -> R.drawable.h_floor_1
                }
            }
            MapBuilding.JBLOCK -> {
                when (floor) {
                    1 -> R.drawable.j_floor_1
                    else -> R.drawable.j_floor_1
                }
            }
        }
    }
}
