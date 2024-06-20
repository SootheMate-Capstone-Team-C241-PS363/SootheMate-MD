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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiMessage = MutableLiveData<String?>()
    val apiMessage: LiveData<String?> = _apiMessage


    fun signUp(name: String, email: String, password: String, passwordConfirmation: String, gender: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _signUpSuccess.value = null
                val token = repository.register(name, email, password, passwordConfirmation, "", gender)
                if (token != null) {
                    val userModel = UserModel(email, token)
                    saveSession(userModel)
                    _signUpSuccess.value = true
                    _apiMessage.value = token
                } else {
                    _apiMessage.value = token
                    _signUpSuccess.value = false
                }
            } catch (e: Exception) {
                Log.e("Error SignUpViewModel", e.message, e)
                _signUpSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}