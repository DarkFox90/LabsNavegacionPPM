package com.example.lab07.characters

import com.example.lab07.Character

data class CharacterDetailsState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)