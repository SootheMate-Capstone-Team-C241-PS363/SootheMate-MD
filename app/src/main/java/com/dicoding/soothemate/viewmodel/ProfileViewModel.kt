package com.dicoding.soothemate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.soothemate.data.UserRepository
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.ApiResponse
import com.dicoding.soothemate.data.api.DetailProfile
import com.dicoding.soothemate.data.api.ProfileData
import com.dicoding.soothemate.data.api.ResetUserCredentials
import com.dicoding.soothemate.data.api.UpdateUserInfo
import com.dicoding.soothemate.data.api.UpdateUserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel (private val repository: UserRepository) : ViewModel() {

    private val _detailProfil = MutableLiveData<ProfileData?>()
    val detailProfile: LiveData<ProfileData?> = _detailProfil

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: LiveData<Boolean?> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailProfile(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService(token).getDetailUser()
        client.enqueue(object : Callback<DetailProfile> {
            override fun onResponse(
                call: Call<DetailProfile>,
                response: Response<DetailProfile>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailProfil.value = response.body()?.data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailProfile>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun changePassword(oldPassword: String, newPassword: String, passwordConfirmation: String, token: String) {
        _isLoading.value = true
        val request = ResetUserCredentials(oldPassword, newPassword, passwordConfirmation)
        val client = ApiConfig.getApiService(token).updatePassword(request)
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == "success") {
                        _isSuccess.value = true
                    } else {
                        Log.e(TAG, "onFailure: ${responseBody?.message}")
                        _isSuccess.value = false
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun updateUserInfo(name: String, gender: String, birthDate: String, token: String) {
        _isLoading.value = true
        val request = UpdateUserInfo(name, gender, birthDate)
        val client = ApiConfig.getApiService(token).updateUserInfo(request)
        client.enqueue(object : Callback<UpdateUserInfoResponse> {
            override fun onResponse(
                call: Call<UpdateUserInfoResponse>,
                response: Response<UpdateUserInfoResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.status == "success") {
                        _isSuccess.value = true
                    } else {
                        Log.e(TAG, "onFailure: ${responseBody?.message}")
                        _isSuccess.value = false
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        private const val TAG = "ProfileViewModel"
    }
}