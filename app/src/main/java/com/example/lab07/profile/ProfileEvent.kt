package com.example.lab07.profile

sealed interface ProfileEvent {
    object OnLogoutClick : ProfileEvent
}