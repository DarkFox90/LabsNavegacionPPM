package com.example.lab07.data.repository

import com.example.lab07.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun syncCharacters()
    fun getCharacters(): Flow<List<Character>>
    fun getFavoriteCharacters(): Flow<List<Character>>
    fun getCharacterById(id: Int): Flow<Character?>
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
    suspend fun clearCharacters()
}