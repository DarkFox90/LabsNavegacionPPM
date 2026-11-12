package com.example.lab07.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorLocationApi(
    private val client: HttpClient
) : LocationApi {

    override suspend fun getLocations(): List<LocationDto> {
        val response: LocationResponseDto = client.get("location").body()
        return response.results
    }
}