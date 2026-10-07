package com.example.lab07.characters

import com.example.lab07.Character
data class CharactersState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)