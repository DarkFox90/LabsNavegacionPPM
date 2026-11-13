package com.example.lab07.data.repository


import com.example.lab07.Character
import com.example.lab07.data.CharacterDao
import com.example.lab07.data.remote.CharacterApi
import com.example.lab07.data.toEntity
import com.example.lab07.data.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val api: CharacterApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override suspend fun syncCharacters() {
        val characters = api.getCharacters()
        dao.insertAll(characters.map { it.toEntity() })
    }

    override fun getCharacters(): Flow<List<Character>> {
        return dao.getAllCharacters().map { entities -> entities.map { it.toModel() } }
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return dao.getFavoriteCharacters().map { entities -> entities.map { it.toModel() } }
    }

    override fun getCharacterById(id: Int): Flow<Character?> {
        return dao.getCharacterById(id).map { it?.toModel() }
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavoriteStatus(id, isFavorite)
    }

    override suspend fun createCharacter(character: Character) {
        dao.insert(character.copy(id = 0).toEntity())
    }

    override suspend fun updateCharacter(character: Character) {
        dao.update(character.toEntity())
    }

    override suspend fun deleteCharacter(id: Int) {
        dao.deleteById(id)
    }

    override suspend fun clearCharacters() {
        dao.clearAll()
    }
}