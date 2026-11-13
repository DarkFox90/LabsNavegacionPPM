package com.example.lab07.data.repository

import com.example.lab07.Location
import com.example.lab07.data.LocationDao
import com.example.lab07.data.remote.LocationApi
import com.example.lab07.data.toEntity
import com.example.lab07.data.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(
    private val api: LocationApi,
    private val dao: LocationDao
) : LocationRepository {

    override suspend fun syncLocations() {
        val locations = api.getLocations()
        dao.insertAll(locations.map { it.toEntity() })
    }

    override fun getLocations(): Flow<List<Location>> {
        return dao.getAllLocations().map { entities -> entities.map { it.toModel() } }
    }

    override fun getLocationById(id: Int): Flow<Location?> {
        return dao.getLocationById(id).map { it?.toModel() }
    }

    override fun getFavoriteLocations(): Flow<List<Location>> {
        return dao.getFavoriteLocations().map { entities -> entities.map { it.toModel() } }
    }

    override fun getNotFavoriteLocations(): Flow<List<Location>> {
        return dao.getNotFavoriteLocations().map { entities -> entities.map { it.toModel() } }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavoriteStatus(id, isFavorite)
    }

    override suspend fun clearLocations() {
        dao.clearAll()
    }
}