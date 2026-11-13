package com.example.lab07.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query("SELECT * FROM locations")
    fun getAllLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE id = :id LIMIT 1")
    fun getLocationById(id: Int): Flow<LocationEntity?>

    @Query("SELECT * FROM locations WHERE isFavorite = 1")
    fun getFavoriteLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM locations WHERE isFavorite = 0")
    fun getNotFavoriteLocations(): Flow<List<LocationEntity>>

    @Query("UPDATE locations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)
    @Query("DELETE FROM locations")
    suspend fun clearAll()
}