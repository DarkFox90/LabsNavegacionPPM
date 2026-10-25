package com.example.lab07.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab07.data.CharacterDb
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.LocationDb
import com.example.lab07.data.UserStore
import com.example.lab07.data.toEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userStore = UserStore(application)
    private var job: Job? = null
    private val database = DatabaseProvider.getDatabase(application)
    private val characterDao = database.characterDao()
    private val locationDao = database.locationDao()

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
            is LoginEvent.OnLoginClick -> {
                job?.cancel()
                job = viewModelScope.launch {
                    val nameToSave = _state.value.nameInput
                    if (nameToSave.isNotBlank()) {
                        _state.update { it.copy(isLoading = true) }

                        val charactersToSync = CharacterDb().getAllCharacters().map { it.toEntity() }
                        val locationsToSync = LocationDb().getAllLocations().map { it.toEntity() }

                        characterDao.insertAll(charactersToSync)
                        locationDao.insertAll(locationsToSync)

                        kotlinx.coroutines.delay(4000)

                        userStore.saveName(nameToSave)
                        _state.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                    }
                }
            }
        }
    }
}