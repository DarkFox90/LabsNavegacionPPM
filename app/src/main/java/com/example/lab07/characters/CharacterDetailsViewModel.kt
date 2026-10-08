package com.example.lab07.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab07.CharacterDb
import com.example.lab07.navigation.CharacterDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
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
            delay(2000)

            val destination = savedStateHandle.toRoute<CharacterDetailsDestination>()
            val idPersonaje = destination.characterId

            val db = CharacterDb()
            val personajeEncontrado = db.getAllCharacters().find { it.id == idPersonaje }

            if (personajeEncontrado != null) {
                _state.update { it.copy(isLoading = false, data = personajeEncontrado) }
            } else {
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    private fun forzarError() {
        job?.cancel()
        _state.update { it.copy(isLoading = false, hasError = true) }
    }
}