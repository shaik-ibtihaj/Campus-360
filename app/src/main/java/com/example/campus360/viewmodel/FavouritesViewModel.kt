package com.example.campus360.viewmodel

import androidx.lifecycle.ViewModel
import com.example.campus360.data.mock.MockData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel : ViewModel() {
    private val _favoriteRoomIds = MutableStateFlow(MockData.favoriteRoomIds.toSet())
    val favoriteRoomIds: StateFlow<Set<String>> = _favoriteRoomIds.asStateFlow()

    private val _favoritePOIIds = MutableStateFlow(MockData.favoritePOIIds.toSet())
    val favoritePOIIds: StateFlow<Set<String>> = _favoritePOIIds.asStateFlow()

    fun toggleRoomFavorite(roomId: String) {
        val current = _favoriteRoomIds.value.toMutableSet()
        if (current.contains(roomId)) {
            current.remove(roomId)
            MockData.favoriteRoomIds.remove(roomId)
        } else {
            current.add(roomId)
            if (!MockData.favoriteRoomIds.contains(roomId)) {
                MockData.favoriteRoomIds.add(roomId)
            }
        }
        _favoriteRoomIds.value = current
    }

    fun togglePOIFavorite(poiId: String) {
        val current = _favoritePOIIds.value.toMutableSet()
        if (current.contains(poiId)) {
            current.remove(poiId)
            MockData.favoritePOIIds.remove(poiId)
        } else {
            current.add(poiId)
            if (!MockData.favoritePOIIds.contains(poiId)) {
                MockData.favoritePOIIds.add(poiId)
            }
        }
        _favoritePOIIds.value = current
    }
}
