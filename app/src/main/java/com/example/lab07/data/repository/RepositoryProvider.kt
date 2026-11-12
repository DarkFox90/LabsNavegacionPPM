package com.example.lab07.data.repository

import android.content.Context
import com.example.lab07.data.DatabaseProvider
import com.example.lab07.data.UserStore
import com.example.lab07.data.remote.HttpClientFactory
import com.example.lab07.data.remote.KtorCharacterApi
import com.example.lab07.data.remote.KtorLocationApi

object RepositoryProvider {
    private val client by lazy { HttpClientFactory.create() }

    fun characterRepository(context: Context): CharacterRepository {
        val dao = DatabaseProvider.getDatabase(context).characterDao()
        return CharacterRepositoryImpl(KtorCharacterApi(client), dao)
    }

    fun locationRepository(context: Context): LocationRepository {
        val dao = DatabaseProvider.getDatabase(context).locationDao()
        return LocationRepositoryImpl(KtorLocationApi(client), dao)
    }

    fun userStore(context: Context): UserStore {
        return UserStore(context)
    }
}