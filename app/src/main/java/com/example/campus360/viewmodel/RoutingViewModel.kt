package com.example.campus360.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoutingViewModel : ViewModel() {
    private val _startLocation = MutableStateFlow<String?>(null)
    val startLocation: StateFlow<String?> = _startLocation.asStateFlow()

    private val _destinationLocation = MutableStateFlow<String?>(null)
    val destinationLocation: StateFlow<String?> = _destinationLocation.asStateFlow()

    private val _isRoutingMode = MutableStateFlow(false)
    val isRoutingMode: StateFlow<Boolean> = _isRoutingMode.asStateFlow()

    fun setDestinationLocation(location: String?) {
        _destinationLocation.value = location
        _isRoutingMode.value = location != null
    }

    fun setStartLocation(location: String?) {
        _startLocation.value = location
    }

    fun clearRoutingState() {
        _startLocation.value = null
        _destinationLocation.value = null
        _isRoutingMode.value = false
    }
}
