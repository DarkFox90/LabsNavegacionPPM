package com.example.lab07.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val isFavorite: Boolean = false
)