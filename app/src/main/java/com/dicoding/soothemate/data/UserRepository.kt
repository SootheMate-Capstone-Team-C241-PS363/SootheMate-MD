package com.dicoding.soothemate.data

import android.util.Log
import com.dicoding.soothemate.data.api.ApiService
import com.dicoding.soothemate.data.api.UserCredentials
import com.dicoding.soothemate.data.api.UserRequest
import com.dicoding.soothemate.data.pref.UserModel
import com.dicoding.soothemate.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun login(email: String, password: String): String? {
        return try {
            val request = UserRequest(email, password)
            val response = apiService.login(request)
            val token = response.data.accessToken
            if (response.status == "success") {
                token
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("login Error", e.message ?: "Unknown error", e)
            null
        }
    }


    suspend fun logout() {
        userPreference.logout()
    }

// <<<<<<< kevin
    suspend fun register(name: String, email: String, password: String,passwordConfirmation: String, birthDate: String, gender: String): String? {
        return try {
            val request = UserCredentials(name, email, password, passwordConfirmation, birthDate, gender)
// =======
//     suspend fun register(name: String, email: String, password: String,paswordConfirmation: String, birthDate: String, gender: String): String? {
//         return try {
//             val request = UserCredentials(name, email, password, paswordConfirmation, birthDate, gender)
// >>>>>>> develop
            val response = apiService.register(request)
            val token = response.data.accessToken
            if (response.status == "success") {
                token
            } else {
                response.message
            }
        } catch (e: Exception) {
            Log.e("register Error", e.message ?: "Unknown error", e)
            null
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}