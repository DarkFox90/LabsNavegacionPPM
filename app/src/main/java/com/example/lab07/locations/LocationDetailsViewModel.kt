package com.example.lab07.locations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.lab07.data.repository.LocationRepository
import com.example.lab07.data.repository.RepositoryProvider
import com.example.lab07.navigation.LocationDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LocationDetailsState())
    val state: StateFlow<LocationDetailsState> = _state.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: LocationDetailsEvent) {
        when (event) {
            is LocationDetailsEvent.CargarDatos -> cargarDatos()
            is LocationDetailsEvent.ForzarError -> forzarError()
            is LocationDetailsEvent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            val destination = savedStateHandle.toRoute<LocationDetailsDestination>()
            val idUbicacion = destination.locationId

            repository.getLocationById(idUbicacion).collectLatest { ubicacion ->
                if (ubicacion != null) {
                    _state.update { it.copy(isLoading = false, data = ubicacion) }
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

    private fun toggleFavorite() {
        val location = _state.value.data ?: return
        viewModelScope.launch {
            repository.updateFavorite(location.id, !location.isFavorite)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                LocationDetailsViewModel(
                    createSavedStateHandle(),
                    RepositoryProvider.locationRepository(application)
                )
            }
        }
    }
}