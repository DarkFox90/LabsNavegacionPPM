package com.example.lab07.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.lab07.Character
import com.example.lab07.data.repository.CharacterRepository
import com.example.lab07.data.repository.RepositoryProvider
import com.example.lab07.navigation.CharacterFormDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {

    private val characterId = savedStateHandle.toRoute<CharacterFormDestination>().characterId

    private val _state = MutableStateFlow(CharacterFormState(id = characterId))
    val state: StateFlow<CharacterFormState> = _state.asStateFlow()

    init {
        if (characterId != null) {
            viewModelScope.launch {
                val character = repository.getCharacterById(characterId).first()
                if (character != null) {
                    _state.update {
                        it.copy(
                            name = character.name,
                            status = character.status,
                            species = character.species,
                            gender = character.gender,
                            image = character.image,
                            isFavorite = character.isFavorite
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: CharacterFormEvent) {
        when (event) {
            is CharacterFormEvent.OnNameChanged -> _state.update { it.copy(name = event.value, showNameError = false) }
            is CharacterFormEvent.OnStatusChanged -> _state.update { it.copy(status = event.value) }
            is CharacterFormEvent.OnSpeciesChanged -> _state.update { it.copy(species = event.value) }
            is CharacterFormEvent.OnGenderChanged -> _state.update { it.copy(gender = event.value) }
            is CharacterFormEvent.OnImageChanged -> _state.update { it.copy(image = event.value) }
            is CharacterFormEvent.OnSaveClick -> guardar()
        }
    }

    private fun guardar() {
        val form = _state.value
        if (form.name.isBlank()) {
            _state.update { it.copy(showNameError = true) }
            return
        }

        viewModelScope.launch {
            val character = Character(
                id = form.id ?: 0,
                name = form.name.trim(),
                status = form.status.trim().ifBlank { "unknown" },
                species = form.species.trim().ifBlank { "unknown" },
                gender = form.gender.trim().ifBlank { "unknown" },
                image = form.image.trim(),
                isFavorite = form.isFavorite
            )

            if (form.id == null) {
                repository.createCharacter(character)
            } else {
                repository.updateCharacter(character)
            }

            _state.update { it.copy(isSaved = true) }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                CharacterFormViewModel(
                    createSavedStateHandle(),
                    RepositoryProvider.characterRepository(application)
                )
            }
        }
    }
}