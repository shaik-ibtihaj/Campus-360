package com.example.campus360

import com.example.campus360.viewmodel.RoutingViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RoutingViewModelTest {

    private lateinit var viewModel: RoutingViewModel

    @Before
    fun setup() {
        viewModel = RoutingViewModel()
    }

    @Test
    fun `setDestinationLocation updates destination and enters routing mode`() {
        val location = "J1610"
        
        viewModel.setDestinationLocation(location)

        assertEquals(location, viewModel.destinationLocation.value)
        assertTrue(viewModel.isRoutingMode.value)
    }

    @Test
    fun `setDestinationLocation to null exits routing mode`() {
        viewModel.setDestinationLocation("Some location")
        assertTrue(viewModel.isRoutingMode.value)

        viewModel.setDestinationLocation(null)

        assertNull(viewModel.destinationLocation.value)
        assertFalse(viewModel.isRoutingMode.value)
    }

    @Test
    fun `setStartLocation updates start location`() {
        val location = "Main Entrance"
        
        viewModel.setStartLocation(location)

        assertEquals(location, viewModel.startLocation.value)
    }

    @Test
    fun `clearRoutingState resets all state`() {
        viewModel.setStartLocation("Start")
        viewModel.setDestinationLocation("End")
        
        viewModel.clearRoutingState()

        assertNull(viewModel.startLocation.value)
        assertNull(viewModel.destinationLocation.value)
        assertFalse(viewModel.isRoutingMode.value)
    }
}
