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
    fun getLocationById(id: Int): Flow<LocationEntity>

    @Query("DELETE FROM locations")
    suspend fun clearAll()
}