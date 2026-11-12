package com.example.lab07.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab07.data.repository.LocationRepository
import com.example.lab07.data.repository.RepositoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationsViewModel(
    private val repository: LocationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LocationsState())
    val state: StateFlow<LocationsState> = _state.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: LocationsEvent) {
        when (event) {
            is LocationsEvent.CargarDatos -> cargarDatos()
            is LocationsEvent.ForzarError -> forzarError()
        }
    }

    private fun cargarDatos() {
        job?.cancel()
        _state.update { it.copy(isLoading = true, hasError = false) }

        job = viewModelScope.launch {
            repository.getLocations().collectLatest { ubicaciones ->
                _state.update { it.copy(isLoading = false, data = ubicaciones) }
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
                LocationsViewModel(RepositoryProvider.locationRepository(application))
            }
        }
    }
}