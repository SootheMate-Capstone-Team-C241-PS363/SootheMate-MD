package com.dicoding.soothemate.di

import android.content.Context
import com.dicoding.soothemate.data.UserRepository
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.pref.UserPreference
import com.dicoding.soothemate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}