package com.example.lab07.data.repository

import com.example.lab07.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun syncLocations()
    fun getLocations(): Flow<List<Location>>
    fun getLocationById(id: Int): Flow<Location?>
    fun getFavoriteLocations(): Flow<List<Location>>
    fun getNotFavoriteLocations(): Flow<List<Location>>
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
    suspend fun clearLocations()
}