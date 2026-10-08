package com.example.lab07.characters

sealed interface CharacterDetailsEvent {
    object CargarDatos : CharacterDetailsEvent
    object ForzarError : CharacterDetailsEvent
}