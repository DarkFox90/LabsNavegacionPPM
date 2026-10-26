package com.example.lab07.characters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab07.data.CharacterDb
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.toModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(application: Application): AndroidViewModel(application) {
    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    private var job: Job? = null
    private val characterDao = DatabaseProvider.getDatabase(application).characterDao()

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
                    characterDao.updateFavoriteStatus(event.characterId, !event.currentIsFavorite)
                }
            }
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            val isFiltered = _state.value.showOnlyFavorites
            val flow = if (isFiltered) characterDao.getFavoriteCharacters() else characterDao.getAllCharacters()

            flow.collectLatest { entities ->
                val personajes = entities.map { it.toModel() }
                _state.update { it.copy(isLoading = false, data = personajes) }
            }
        }
    }

    private fun forzarError() {
        job?.cancel()
        _state.update { it.copy(isLoading = false, hasError = true) }
    }
}