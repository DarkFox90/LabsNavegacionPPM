package com.example.lab07.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)

@Serializable
data class LocationResponseDto(
    val results: List<LocationDto>
)
