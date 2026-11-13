package com.example.lab07.profile

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

class ProfileViewModel(
    private val userStore: UserStore,
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {
    private var job: Job? = null

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userStore.userName.collect { name ->
                _state.update { it.copy(userName = name ?: "") }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        if (event is ProfileEvent.OnLogoutClick) {
            job?.cancel()
            job = viewModelScope.launch {
                characterRepository.clearCharacters()
                locationRepository.clearLocations()
                userStore.clearName()
                _state.update { it.copy(isLoggedOut = true) }
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                ProfileViewModel(
                    RepositoryProvider.userStore(application),
                    RepositoryProvider.characterRepository(application),
                    RepositoryProvider.locationRepository(application)
                )
            }
        }
    }
}