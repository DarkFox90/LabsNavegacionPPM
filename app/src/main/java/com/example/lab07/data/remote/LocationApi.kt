package com.example.lab07.data.remote

interface LocationApi {
    suspend fun getLocations(): List<LocationDto>
}