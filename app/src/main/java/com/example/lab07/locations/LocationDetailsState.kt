package com.example.lab07.locations

import com.example.lab07.Location

data class LocationDetailsState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)