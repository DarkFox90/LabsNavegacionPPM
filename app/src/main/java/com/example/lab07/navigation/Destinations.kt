package com.example.lab07.navigation

import kotlinx.serialization.Serializable

@Serializable data object LoginDestination
@Serializable data object HomeDestination
@Serializable data class DetailsDestination(val characterId: Int)