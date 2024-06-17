package com.dicoding.soothemate.ui.history

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.dicoding.soothemate.R
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.HistoryDetailData
import com.dicoding.soothemate.data.api.HistoryDetailResponse
import com.dicoding.soothemate.data.api.SavePredictData
import com.dicoding.soothemate.data.api.SavePredictDataResult
import com.dicoding.soothemate.data.api.SavePredictResponse
import com.dicoding.soothemate.data.pref.UserPreference
import com.dicoding.soothemate.databinding.ActivityHistoryDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var historyId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyId = intent.getStringExtra("HISTORY_ID") ?: ""

        supportActionBar?.hide()
        initSpinner()
        initView()
        initTextWatcher()
        if (historyId.isNotEmpty()) {
            loadHistoryDetail(historyId)
        }
    }

    private fun initTextWatcher() {
        val textFields = listOf(
            binding.etAge,
            binding.etSleepDuration,
            binding.etSleepQuality,
            binding.etPhysicalActivity,
            binding.etWorkingHours,
            binding.etBloodPressure,
            binding.etHeartRate,
            binding.etDailySteps
        )

        textFields.forEach { textField ->
            textField.addTextChangedListener {
                checkField()
            }
        }
    }

    private fun initView() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.btnBackToHistory.setOnClickListener {
            finish()
        }

        binding.btnEditAndSave.setOnClickListener {
            saveDetails()
        }
    }

    private fun initSpinner() {
        val genderAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_gender,
            android.R.layout.simple_spinner_dropdown_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etGender.adapter = genderAdapter

        val bmiAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_bmi,
            android.R.layout.simple_spinner_dropdown_item
        )
        bmiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.etBMICategory.adapter = bmiAdapter
    }

    private fun checkField() {
        binding.btnEditAndSave.isEnabled = listOf(
            binding.etAge,
            binding.etSleepDuration,
            binding.etSleepQuality,
            binding.etPhysicalActivity,
            binding.etWorkingHours,
            binding.etBloodPressure,
            binding.etHeartRate,
            binding.etDailySteps
        ).all { it.text.toString().isNotEmpty() }
    }

    private fun loadHistoryDetail(historyId: String) {
        val apiService = ApiConfig.getApiService("")
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getDetailHistory(historyId).enqueue(object : Callback<HistoryDetailResponse> {
                override fun onResponse(call: Call<HistoryDetailResponse>, response: Response<HistoryDetailResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { historyDetail ->
                            runOnUiThread {
                                updateUI(historyDetail)
                            }
                        }
                    } else {
                        // Handle errors
                    }
                }

                override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    private fun updateUI(detail: HistoryDetailData) {
        binding.etAge.setText(detail.age.toString())
        binding.etSleepDuration.setText(detail.sleepDuration.toString())
        // Continue for other fields
    }

    private fun saveDetails() {
        val updatedDetail = SavePredictData(
            gender = binding.etGender.selectedItem.toString(),
            age = binding.etAge.text.toString().toInt(),
            sleepDuration = binding.etSleepDuration.text.toString().toInt(),
            qualityOfSleep = binding.etSleepQuality.text.toString().toInt(),
            physicalActivityLevel = binding.etPhysicalActivity.text.toString().toInt(),
            minWorkingHours = binding.etWorkingHours.text.toString().toInt(),
            maxWorkingHours = binding.etWorkingHours.text.toString().toInt(),
            result = SavePredictDataResult(
                stressLevel = binding.etStressLevel.text.toString().toInt(),
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString()
            )
        )

        val token = UserPreference.getSession().first().token

        val apiService = ApiConfig.getApiService(token)
        apiService.savePredictStress(updatedDetail).enqueue(object : Callback<SavePredictResponse> {
            override fun onResponse(call: Call<SavePredictResponse>, response: Response<SavePredictResponse>) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@HistoryDetailActivity, "Details updated successfully.", Toast.LENGTH_SHORT).show()
                        finish() // Optionally close the activity if needed
                    } else {
                        Toast.makeText(this@HistoryDetailActivity, "Failed to update details: ${response.errorBody()?.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<SavePredictResponse>, t: Throwable) {
                runOnUiThread {
                    Toast.makeText(this@HistoryDetailActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}