package com.example.lab07.locations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.LocationDb
import com.example.lab07.data.toModel
import com.example.lab07.navigation.LocationDetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(LocationDetailsState())
    val state: StateFlow<LocationDetailsState> = _state.asStateFlow()

    private var job: Job? = null
    private val locationDao = DatabaseProvider.getDatabase(application).locationDao()

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
            val destination = savedStateHandle.toRoute<LocationDetailsDestination>()
            val idUbicacion = destination.locationId

            locationDao.getLocationById(idUbicacion).collectLatest { entity ->
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