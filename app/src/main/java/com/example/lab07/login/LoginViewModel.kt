package com.example.lab07.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lab07.data.UserStore
import com.example.lab07.data.repository.CharacterRepository
import com.example.lab07.data.repository.LocationRepository
import com.example.lab07.data.repository.RepositoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userStore: UserStore,
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private var job: Job? = null

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userStore.userName.collect { name ->
                if (!name.isNullOrBlank()) {
                    _state.update { it.copy(isLoginSuccessful = true) }
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnNameChanged -> {
                _state.update { it.copy(nameInput = event.newName) }
            }
            is LoginEvent.OnLoginClick -> login()
        }
    }

    private fun login() {
        val nameToSave = _state.value.nameInput
        if (nameToSave.isBlank()) return

        job?.cancel()
        job = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, hasError = false) }
            try {
                characterRepository.syncCharacters()
                locationRepository.syncLocations()
                userStore.saveName(nameToSave)
                _state.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, hasError = true) }
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                LoginViewModel(
                    RepositoryProvider.userStore(application),
                    RepositoryProvider.characterRepository(application),
                    RepositoryProvider.locationRepository(application)
                )
            }
        }
    }
}