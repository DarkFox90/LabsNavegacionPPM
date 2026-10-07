package com.example.lab07.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab07.CharacterDb
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel : ViewModel() {
    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    init {
        cargarDatos()
    }

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.CargarDatos -> cargarDatos()
            is CharactersEvent.ForzarError -> forzarError()
        }
    }

    private fun cargarDatos() {
        _state.update { it.copy(isLoading = true, hasError = false) }

        viewModelScope.launch {
            delay(4000)
            val db = CharacterDb()
            val personajes = db.getAllCharacters()
            _state.update { it.copy(isLoading = false, data = personajes) }
        }
    }

    private fun forzarError() {
        _state.update { it.copy(isLoading = false, hasError = true) }
    }
}