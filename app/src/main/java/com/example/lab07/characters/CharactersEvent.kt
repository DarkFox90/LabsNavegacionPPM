package com.example.lab07.characters

sealed interface CharactersEvent {
    object CargarDatos : CharactersEvent
    object ForzarError : CharactersEvent

    data class ToggleFavorite(val characterId: Int, val currentIsFavorite: Boolean) : CharactersEvent
    object ToggleFilter : CharactersEvent
}
