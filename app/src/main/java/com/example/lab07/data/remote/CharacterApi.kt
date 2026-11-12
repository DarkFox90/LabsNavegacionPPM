package com.example.lab07.data.remote

interface CharacterApi {
    suspend fun getCharacters(): List<CharacterDto>
}