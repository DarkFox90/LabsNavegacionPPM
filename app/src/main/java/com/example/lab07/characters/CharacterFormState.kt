package com.example.lab07.characters

data class CharacterFormState(
    val id: Int? = null,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isFavorite: Boolean = false,
    val showNameError: Boolean = false,
    val isSaved: Boolean = false
)