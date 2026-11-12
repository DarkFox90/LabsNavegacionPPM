package com.example.lab07.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class KtorCharacterApi(
    private val client: HttpClient
) : CharacterApi {

    override suspend fun getCharacters(): List<CharacterDto> {
        val response: CharacterResponseDto = client.get("character").body()
        return response.results
    }
}