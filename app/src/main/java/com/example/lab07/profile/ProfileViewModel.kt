package com.example.lab07.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab07.data.UserStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userStore = UserStore(application)
    private var job: Job? = null

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userStore.userName.collect { name ->
                _state.update { it.copy(userName = name?: "") }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        if (event is ProfileEvent.OnLogoutClick) {
            job?.cancel()
            job = viewModelScope.launch {
                userStore.clearName()
                _state.update { it.copy(isLoggedOut = true) }
            }
        }
    }
}