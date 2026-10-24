package com.example.lab07.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getCharacterById(id: Int): Flow<CharacterEntity>

    @Query("SELECT * FROM characters WHERE isFavorite = 1")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>

    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    @Query("DELETE FROM characters")
    suspend fun clearAll()
}