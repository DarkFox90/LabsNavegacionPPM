package com.example.lab07.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.toModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationsViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(LocationsState())
    val state: StateFlow<LocationsState> = _state.asStateFlow()

    private var job: Job? = null
    private val locationDao = DatabaseProvider.getDatabase(application).locationDao()

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
            locationDao.getAllLocations().collectLatest { entities ->
                val ubicaciones = entities.map { it.toModel() }
                _state.update { it.copy(isLoading = false, data = ubicaciones) }
            }
        }
    }

    private fun forzarError() {
        job?.cancel()
        _state.update { it.copy(isLoading = false, hasError = true) }
    }
}