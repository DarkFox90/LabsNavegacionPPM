package com.example.lab07.login

sealed interface LoginEvent {
    data class OnNameChanged(val newName: String) : LoginEvent
    object OnLoginClick : LoginEvent
}