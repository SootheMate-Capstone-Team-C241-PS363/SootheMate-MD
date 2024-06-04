package com.dicoding.soothemate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.soothemate.data.UserRepository
import com.dicoding.soothemate.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData<Boolean?>()
    val loginSuccess: LiveData<Boolean?> = _loginSuccess

    fun login(email: String, password: String) {
        _isLoading.value = true
        _loginSuccess.value = null
        viewModelScope.launch {
            try {
                val token = repository.login(email, password)
                if (token != null) {
                    _isLoading.value = false
                    val userModel = UserModel(email, token)
                    saveSession(userModel)
                    _loginSuccess.value = true
                } else {
                    _isLoading.value = false
                    _loginSuccess.value = false
                }
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("Error", e.message, e)
                _loginSuccess.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}