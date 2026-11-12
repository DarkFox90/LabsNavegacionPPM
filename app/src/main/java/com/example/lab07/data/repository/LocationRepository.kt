package com.example.lab07.data.repository

import com.example.lab07.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun syncLocations()
    fun getLocations(): Flow<List<Location>>
    fun getLocationById(id: Int): Flow<Location?>
    suspend fun clearLocations()
}