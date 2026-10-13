package com.example.lab07

import android.content.Context
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserStore(private val context: Context) {
    private val userNameKey = stringPreferencesKey("user_name")

    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[userNameKey]
    }

    suspend fun saveName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[userNameKey] = name
        }
    }

    suspend fun clearName() {
        context.dataStore.edit { preferences ->
            preferences.remove(userNameKey)
        }
    }
}