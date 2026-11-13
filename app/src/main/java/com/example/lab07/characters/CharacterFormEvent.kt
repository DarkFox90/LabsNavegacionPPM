package com.example.lab07.characters

sealed interface CharacterFormEvent {
    data class OnNameChanged(val value: String) : CharacterFormEvent
    data class OnStatusChanged(val value: String) : CharacterFormEvent
    data class OnSpeciesChanged(val value: String) : CharacterFormEvent
    data class OnGenderChanged(val value: String) : CharacterFormEvent
    data class OnImageChanged(val value: String) : CharacterFormEvent
    object OnSaveClick : CharacterFormEvent
}