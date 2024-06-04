package com.dicoding.soothemate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.soothemate.data.UserRepository
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.ApiResponse
import com.dicoding.soothemate.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _logoutSuccess = MutableLiveData<Boolean?>()
    val logoutSuccess: LiveData<Boolean?> = _logoutSuccess

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout()  {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun logoutPost(token: String) {
        val client = ApiConfig.getApiService(token).logout()
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if (response.isSuccessful) {
                    _logoutSuccess.value = true
                    logout()
                } else {
                    _logoutSuccess.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}