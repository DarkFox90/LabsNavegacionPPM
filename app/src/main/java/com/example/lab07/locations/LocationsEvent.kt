package com.example.lab07.locations

import com.example.lab07.Location

sealed interface LocationsEvent {
    object CargarDatos : LocationsEvent
    object ForzarError : LocationsEvent
    data class ToggleFavorite(val locationId: Int, val currentIsFavorite: Boolean) : LocationsEvent
    data class ChangeFilter(val filter: LocationFilter) : LocationsEvent

}