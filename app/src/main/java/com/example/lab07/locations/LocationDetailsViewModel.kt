package com.example.lab07.locations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab07.LocationDb
import com.example.lab07.navigation.LocationDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(LocationDetailsState())
    val state: StateFlow<LocationDetailsState> = _state.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: LocationDetailsEvent) {
        when (event) {
            is LocationDetailsEvent.CargarDatos -> cargarDatos()
            is LocationDetailsEvent.ForzarError -> forzarError()
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            delay(2000)

            val destination = savedStateHandle.toRoute<LocationDetailsDestination>()
            val idUbicacion = destination.locationId

            val db = LocationDb()
            val ubicacionEncontrada = db.getAllLocations().find { it.id == idUbicacion }

            if (ubicacionEncontrada != null) {
                _state.update { it.copy(isLoading = false, data = ubicacionEncontrada) }
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