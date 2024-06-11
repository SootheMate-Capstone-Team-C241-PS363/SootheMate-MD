package com.dicoding.soothemate.viewmodel

import android.util.Log
import android.widget.Spinner
import androidx.core.view.size
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CustomEditText
import com.dicoding.soothemate.customviews.CustomSelectOption
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.ApiResponse
import com.dicoding.soothemate.data.api.PredictResponse
import com.dicoding.soothemate.data.api.PredictStressData
import com.dicoding.soothemate.data.api.ProfileData
import com.dicoding.soothemate.data.api.ResetUserCredentials
import com.dicoding.soothemate.data.api.UpdateUserInfo
import com.dicoding.soothemate.data.api.UpdateUserInfoResponse
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictViewModel : ViewModel() {
    private val _stressValue = MutableLiveData<Int?>()
    val stressValue: LiveData<Int?> = _stressValue

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: LiveData<Boolean?> = _isSuccess

    private val _apiMessage = MutableLiveData<String?>()
    val apiMessage: LiveData<String?> = _apiMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun predictStress(gender: String, age: Int, sleepDuration: Int, qualityOfSleep: Int, physicalActivityLevel: Int, minWorkingHours: Int, maxWorkingHours: Int, bmiCategory: String?, bloodPressure: String?, heartRate: Int?, dailySteps: Int?, token: String) {

        _isLoading.value = true

        val request = PredictStressData(minWorkingHours, maxWorkingHours, dailySteps, gender, physicalActivityLevel, bmiCategory, sleepDuration, heartRate, qualityOfSleep, bloodPressure, age)

        val client = ApiConfig.getApiService(token).predictStress(request)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    if (responseBody != null && responseBody.status == "success") {
                        _stressValue.value = responseBody.data.stressLevel
                        _apiMessage.value = responseBody.message
                        _isSuccess.value = true
                    } else {
                        Log.e("", "onFailure: ${responseBody?.message}")
                        _isSuccess.value = false
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) {
                        try {
                            JSONObject(errorBody).getString("message")
                        } catch (e: JSONException) {
                            "Unknown error"
                        }
                    } else {
                        response.message()
                    }
                    Log.e("", "onFailure: $errorMessage")
                    _apiMessage.value = errorMessage
                    _isSuccess.value = false
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("", "onFailure: ${t.message.toString()}")
            }
        })
    }


}