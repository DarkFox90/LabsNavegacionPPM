package com.example.lab07.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab07.data.CharacterDb
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.toModel
import com.example.lab07.navigation.CharacterDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(CharacterDetailsState())
    val state: StateFlow<CharacterDetailsState> = _state.asStateFlow()

    private var job: Job? = null
    private val characterDao = DatabaseProvider.getDatabase(application).characterDao()

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

            characterDao.getCharacterById(idPersonaje).collectLatest { entity ->
                if (entity != null) {
                    _state.update { it.copy(isLoading = false, data = entity.toModel()) }
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
}