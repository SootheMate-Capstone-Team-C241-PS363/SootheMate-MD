package com.dicoding.soothemate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.soothemate.data.UserRepository
import com.dicoding.soothemate.data.pref.UserModel
import kotlinx.coroutines.launch

class SignUpViewModel (private val repository: UserRepository) : ViewModel() {

    private val _signUpSuccess = MutableLiveData<Boolean?>()
    val signUpSuccess: LiveData<Boolean?> = _signUpSuccess

    fun signUp(name: String, email: String, password: String, gender: String, birthDate: String) {
        viewModelScope.launch {
            try {
                _signUpSuccess.value = null
                val token = repository.register(name, email, password, gender, birthDate)
                if (token != null) {
                    val userModel = UserModel(email, token)
                    saveSession(userModel)
                    _signUpSuccess.value = true
                } else {
                    _signUpSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("Error SignUpViewModel", e.message, e)
                _signUpSuccess.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}