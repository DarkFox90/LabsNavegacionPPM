package com.example.lab07.navigation

import kotlinx.serialization.Serializable

@Serializable data object LoginDestination
@Serializable data object HomeDestination
@Serializable data class DetailsDestination(val characterId: Int)

// characters
@Serializable data object CharactersGraph

@Serializable data object CharactersListDestination

@Serializable data class CharacterDetailsDestination(val characterId: Int)

// Locations
@Serializable data object LocationsGraph

@Serializable data object LocationsListDestination

@Serializable data class LocationDetailsDestination(val locationId: Int)

// profile
@Serializable data object ProfileDestination