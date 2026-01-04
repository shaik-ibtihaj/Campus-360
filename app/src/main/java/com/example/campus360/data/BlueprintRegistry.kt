package com.example.campus360.data

import com.example.campus360.R

enum class MapBuilding(val displayName: String, val maxFloors: Int) {
    HBLOCK("H Block", 2),
    JBLOCK("J Block", 2)
}

object BlueprintRegistry {
    fun getBlueprint(building: MapBuilding, floor: Int): Int {
        return when (building) {
            MapBuilding.HBLOCK -> {
                when (floor) {
                    1 -> R.drawable.h_floor_1
                    2 -> R.drawable.h_floor_2
                    else -> R.drawable.h_floor_1
                }
            }
            MapBuilding.JBLOCK -> {
                when (floor) {
                    1 -> R.drawable.j_floor_1
                    2 -> R.drawable.j_floor_2
                    else -> R.drawable.j_floor_1
                }
            }
        }
    }
}
