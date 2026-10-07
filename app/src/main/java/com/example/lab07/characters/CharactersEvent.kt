package com.example.lab07.characters

sealed interface CharactersEvent {
    object CargarDatos : CharactersEvent
    object ForzarError : CharactersEvent
}
