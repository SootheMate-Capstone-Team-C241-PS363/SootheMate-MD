package com.dicoding.soothemate.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.DataItem
import com.dicoding.soothemate.data.api.HistoryDetailResponse
import com.dicoding.soothemate.data.api.HistoryResponse
import com.dicoding.soothemate.data.api.PredictData
import com.dicoding.soothemate.data.api.PredictResponse
import com.dicoding.soothemate.data.api.PredictStressData
import com.dicoding.soothemate.data.api.SavePredictData
import com.dicoding.soothemate.data.api.SavePredictDataResult
import com.dicoding.soothemate.data.api.SavePredictResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictViewModel : ViewModel() {
    private val _stressValue = MutableLiveData<PredictData?>()
    val stressValue: LiveData<PredictData?> = _stressValue

    private val _stressHistoryValue = MutableLiveData<List<DataItem?>?>()
    val stressHistoryValue: LiveData<List<DataItem?>?> = _stressHistoryValue

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
                        _stressValue.value = responseBody.data
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
                _stressValue.value = null
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun savePredictStress(gender: String, age: Int, sleepDuration: Int, qualityOfSleep: Int, physicalActivityLevel: Int, minWorkingHours: Int, maxWorkingHours: Int, stressLevel: Int, stressTitle: String, stressDesc: String, token: String ) {

        _isLoading.value = true

        val request = SavePredictData(gender, age, sleepDuration, qualityOfSleep, physicalActivityLevel, minWorkingHours, maxWorkingHours, result = SavePredictDataResult(stressLevel = stressLevel, title = stressTitle , description = stressDesc))

        val client = ApiConfig.getApiService(token).savePredictStress(request)
        client.enqueue(object : Callback<SavePredictResponse> {
            override fun onResponse(
                call: Call<SavePredictResponse>,
                response: Response<SavePredictResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    if (responseBody != null && responseBody.status == "success") {
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

            override fun onFailure(call: Call<SavePredictResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getHistory (filter: String?, token: String ) {
        _isLoading.value = true

        val client = ApiConfig.getApiService(token).getHistory(filter)
        client.enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(
                call: Call<HistoryResponse>,
                response: Response<HistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    if (responseBody != null && responseBody.status == "success") {
                        _apiMessage.value = responseBody.message
                        _stressHistoryValue.value = responseBody.data
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

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("", "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailHistory (id: String, token: String ) {
        _isLoading.value = true

        val client = ApiConfig.getApiService(token).getDetailHistory(id)
        client.enqueue(object : Callback<HistoryDetailResponse> {
            override fun onResponse(
                call: Call<HistoryDetailResponse>,
                response: Response<HistoryDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    _isLoading.value = false
                    if (responseBody != null && responseBody.status == "success") {
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

            override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _isSuccess.value = false
                Log.e("", "onFailure: ${t.message.toString()}")
            }
        })
    }


}