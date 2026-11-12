package com.example.lab07.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab07.data.repository.CharacterRepository
import com.example.lab07.data.repository.RepositoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.CargarDatos -> cargarDatos()
            is CharactersEvent.ForzarError -> forzarError()
            is CharactersEvent.ToggleFilter -> {
                _state.update { it.copy(showOnlyFavorites = !it.showOnlyFavorites) }
                cargarDatos()
            }
            is CharactersEvent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.updateFavorite(event.characterId, !event.currentIsFavorite)
                }
            }
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            val isFiltered = _state.value.showOnlyFavorites
            val flow = if (isFiltered) repository.getFavoriteCharacters() else repository.getCharacters()

            flow.collectLatest { personajes ->
                _state.update { it.copy(isLoading = false, data = personajes) }
            }
        }
    }

    private fun forzarError() {
        job?.cancel()
        _state.update { it.copy(isLoading = false, hasError = true) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                CharactersViewModel(RepositoryProvider.characterRepository(application))
            }
        }
    }
}