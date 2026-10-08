package com.example.lab07.locations

sealed interface LocationDetailsEvent {
    object CargarDatos : LocationDetailsEvent
    object ForzarError : LocationDetailsEvent
}