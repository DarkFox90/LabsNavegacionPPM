package com.example.lab07.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.lab07.data.repository.CharacterRepository
import com.example.lab07.data.repository.RepositoryProvider
import com.example.lab07.navigation.CharacterDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterDetailsState())
    val state: StateFlow<CharacterDetailsState> = _state.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: CharacterDetailsEvent) {
        when (event) {
            is CharacterDetailsEvent.CargarDatos -> cargarDatos()
            is CharacterDetailsEvent.ForzarError -> forzarError()
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            val destination = savedStateHandle.toRoute<CharacterDetailsDestination>()
            val idPersonaje = destination.characterId

            repository.getCharacterById(idPersonaje).collectLatest { personaje ->
                if (personaje != null) {
                    _state.update { it.copy(isLoading = false, data = personaje) }
                } else {
                    _state.update { it.copy(isLoading = false, hasError = true) }
                }
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
                CharacterDetailsViewModel(
                    createSavedStateHandle(),
                    RepositoryProvider.characterRepository(application)
                )
            }
        }
    }
}