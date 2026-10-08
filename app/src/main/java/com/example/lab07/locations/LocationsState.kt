package com.example.lab07.locations

import com.example.lab07.Location

data class LocationsState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)
