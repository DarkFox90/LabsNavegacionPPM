package com.example.lab07.login

data class LoginState(
    val nameInput: String = "",
    val isLoginSuccessful: Boolean = false,
    val isLoading: Boolean = false
)