package com.example.lab07.locations

import com.example.lab07.Location

sealed interface LocationsEvent {
    object CargarDatos : LocationsEvent
    object ForzarError : LocationsEvent
}