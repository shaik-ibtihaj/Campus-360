package com.example.campus360

import com.example.campus360.data.mock.MockData
import com.example.campus360.viewmodel.FavoritesViewModel
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoritesViewModelTest {

    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        // Clear or reset MockData state if needed, but since it's a prototype we just use it
        viewModel = FavoritesViewModel()
    }

    @Test
    fun `toggleRoomFavorite adds room to favorites when not present`() {
        val roomId = "test_room"
        if (MockData.favoriteRoomIds.contains(roomId)) {
            MockData.favoriteRoomIds.remove(roomId)
        }
        
        viewModel = FavoritesViewModel() // Re-init to pick up MockData state
        assertFalse(viewModel.favoriteRoomIds.value.contains(roomId))

        viewModel.toggleRoomFavorite(roomId)

        assertTrue(viewModel.favoriteRoomIds.value.contains(roomId))
        assertTrue(MockData.favoriteRoomIds.contains(roomId))
    }

    @Test
    fun `toggleRoomFavorite removes room from favorites when present`() {
        val roomId = "j1610" // Existing in MockData
        if (!MockData.favoriteRoomIds.contains(roomId)) {
            MockData.favoriteRoomIds.add(roomId)
        }
        
        viewModel = FavoritesViewModel()
        assertTrue(viewModel.favoriteRoomIds.value.contains(roomId))

        viewModel.toggleRoomFavorite(roomId)

        assertFalse(viewModel.favoriteRoomIds.value.contains(roomId))
        assertFalse(MockData.favoriteRoomIds.contains(roomId))
    }

    @Test
    fun `togglePOIFavorite adds poi to favorites when not present`() {
        val poiId = "test_poi"
        if (MockData.favoritePOIIds.contains(poiId)) {
            MockData.favoritePOIIds.remove(poiId)
        }
        
        viewModel = FavoritesViewModel()
        assertFalse(viewModel.favoritePOIIds.value.contains(poiId))

        viewModel.togglePOIFavorite(poiId)

        assertTrue(viewModel.favoritePOIIds.value.contains(poiId))
        assertTrue(MockData.favoritePOIIds.contains(poiId))
    }
}
