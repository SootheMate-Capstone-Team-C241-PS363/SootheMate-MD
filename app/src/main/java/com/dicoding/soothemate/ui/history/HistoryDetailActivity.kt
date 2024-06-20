package com.dicoding.soothemate.ui.history

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import com.dicoding.soothemate.R
import com.dicoding.soothemate.data.api.ApiConfig
import com.dicoding.soothemate.data.api.HistoryDetailData
import com.dicoding.soothemate.data.api.HistoryDetailResponse
import com.dicoding.soothemate.databinding.ActivityHistoryDetailBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.predict.result.ResultActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var historyId: String

    var gender: String? = null
    var age : Int? = null
    var sleepDuration : Int? = null
    var qualityOfSleep : Int? = null
    var physicalActivityLevel : Int? = null
    var minWorkingHours : Int? = null
    var maxWorkingHours : Int? = null
    var bmi : String? = null
    var bloodPressure : String? = null
    var heartRate : Int? = null
    var dailySteps : Int? = null

    private val predictViewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        historyId = intent.getStringExtra("HISTORY_ID") ?: ""

        mainViewModel.getSession().observe(this) {
            var token = it.token
            loadHistoryDetail(historyId, token)
        }

        predictViewModel.stressValue.observe(this) { stressLevel ->
            if (stressLevel != null) {
                Intent(this, ResultActivity::class.java).also {
                    it.putExtra(ResultActivity.STRESS_VALUE, stressLevel.stressLevel.toString())
                    it.putExtra(ResultActivity.STRESS_TITLE, stressLevel.title)
                    it.putExtra(ResultActivity.STRESS_DESC, stressLevel.description)
                    it.putExtra(ResultActivity.GENDER, gender.toString())
                    it.putExtra(ResultActivity.AGE, age.toString())
                    it.putExtra(ResultActivity.SLEEP_DURATION, sleepDuration.toString())
                    it.putExtra(ResultActivity.SLEEP_QUALITY, qualityOfSleep.toString())
                    it.putExtra(ResultActivity.PHYSICAL_ACTIVITY, physicalActivityLevel.toString())
                    it.putExtra(ResultActivity.MIN_WORKING_HOURS, minWorkingHours.toString())
                    it.putExtra(ResultActivity.MAX_WORKING_HOURS, maxWorkingHours.toString())
                    it.putExtra(ResultActivity.BMI, bmi.toString())
                    it.putExtra(ResultActivity.BLOOD_PRESSURE, bloodPressure.toString())
                    it.putExtra(ResultActivity.HEART_RATE, heartRate.toString())
                    it.putExtra(ResultActivity.STEPS, dailySteps.toString())


                    startActivity(it)
                }
            }
        }

        dropdownComponent()
        initSpinner()
        initView()
        isAllFilled()
        updateStressLevel()
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

    private fun loadHistoryDetail(historyId: String, token: String) {
        val apiService = ApiConfig.getApiService(token)
        CoroutineScope(Dispatchers.IO).launch {
            binding.progressBar.visibility = View.VISIBLE
            apiService.getDetailHistory(historyId).enqueue(object : Callback<HistoryDetailResponse> {
                override fun onResponse(call: Call<HistoryDetailResponse>, response: Response<HistoryDetailResponse>) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.GONE
                        response.body()?.data?.let { historyDetail ->
                            runOnUiThread {
                                updateUI(historyDetail)
                            }
                        }
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<HistoryDetailResponse>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }

    private fun updateUI(detail: HistoryDetailData) {
        val genderArray = resources.getStringArray(R.array.dropdown_gender)
        val ageArray = resources.getStringArray(R.array.dropdown_age)
        val sleepDurationArray = resources.getStringArray(R.array.sleep_duration)
        val qualityOfSleepArray = resources.getStringArray(R.array.sleep_quality)
        val minWorkingHoursArray = resources.getStringArray(R.array.working_hours)
        val maxWorkingHoursArray = resources.getStringArray(R.array.working_hours)
        val bmiArray = resources.getStringArray(R.array.dropdown_bmi)
        val heartRateArray = resources.getStringArray(R.array.dropdown_heart_rate)

        val selectedGender = detail.gender
        val genderIndex = genderArray.indexOf(selectedGender).takeIf { it >= 0 } ?: 0
        binding.etGender.setSelection(genderIndex)

        val selectedAge = detail.age.toString()
        val ageIndex = ageArray.indexOf(selectedAge).takeIf { it >= 0 } ?: 0
        binding.etAge.setSelection(ageIndex)

        val selectedSleepDuration = detail.sleepDuration.toString()
        val sleepDurationIndex = sleepDurationArray.indexOf(selectedSleepDuration).takeIf { it >= 0 } ?: 0
        binding.etSleepDuration.setSelection(sleepDurationIndex)

        val selectedQualityOfSleep = detail.qualityOfSleep.toString()
        val qualityOfSleepIndex = qualityOfSleepArray.indexOf(selectedQualityOfSleep).takeIf { it >= 0 } ?: 0
        binding.etSleepQuality.setSelection(qualityOfSleepIndex)

        binding.etPhysicalActivity.setText(detail.physicalActivityLevel.toString())

        val selectedMinWorkingHours = detail.minWorkingHours.toString()
        val minWorkingHoursIndex = minWorkingHoursArray.indexOf(selectedMinWorkingHours).takeIf { it >= 0 } ?: 0
        binding.etMinWorkingHours.setSelection(minWorkingHoursIndex)

        val selectedMaxWorkingHours = detail.maxWorkingHours.toString()
        val maxWorkingHoursIndex = maxWorkingHoursArray.indexOf(selectedMaxWorkingHours).takeIf { it >= 0 } ?: 0
        binding.etWorkingHours.setSelection(maxWorkingHoursIndex)

        val selectedBmi = detail.bmiCategory
        val bmiIndex = bmiArray.indexOf(selectedBmi).takeIf { it >= 0 } ?: 0
        binding.etBMICategory.setSelection(bmiIndex)

        binding.etBloodPressure.setText(detail.bloodPressure)

        val selectedHeartRate = detail.heartRate.toString()
        val heartRateIndex = heartRateArray.indexOf(selectedHeartRate).takeIf { it >= 0 } ?: 0
        binding.etHeartRate.setSelection(heartRateIndex)

        binding.etDailySteps.setText(detail.dailySteps.toString())
    }

    private fun saveDetails() {
        val gender = binding.etGender.selectedItem.toString()
        val age = binding.etAge.selectedItem.toString().toIntOrNull() ?: 0
        val sleepDuration = binding.etSleepDuration.selectedItem.toString().toIntOrNull() ?: 0
        val qualityOfSleep = binding.etSleepQuality.selectedItem.toString().toIntOrNull() ?: 0
        val physicalActivityLevel = binding.etPhysicalActivity.text.toString().toInt()
        val minWorkingHours = binding.etMinWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
        val maxWorkingHours = binding.etWorkingHours.selectedItem.toString().toIntOrNull() ?: 0

        mainViewModel.getSession().observe(this) {
            var token = it.token
            predictViewModel.predictStress(gender = gender, age = age, sleepDuration = sleepDuration, qualityOfSleep = qualityOfSleep, physicalActivityLevel = physicalActivityLevel, minWorkingHours = minWorkingHours, maxWorkingHours = maxWorkingHours, null, null, null, null, token)
        }


    }

    private fun setSpinnerAdapter(spinner: Spinner, arrayResId: Int) {
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(arrayResId)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
    }


    private fun dropdownComponent() {
        binding.apply {
            val spinnerGender: Spinner = etGender
            val spinnerAge: Spinner = etAge
            val spinnerSleepDuration: Spinner = etSleepDuration
            val qualityOfSleep: Spinner = etSleepQuality
            val spinnerMinWorkingHours: Spinner = etMinWorkingHours
            val spinnerMaksWorkingHours: Spinner = etWorkingHours
            val bmi: Spinner = etBMICategory
            val heartRate: Spinner = etHeartRate

            setSpinnerAdapter(spinnerGender, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerAge, R.array.dropdown_age)
            setSpinnerAdapter(spinnerSleepDuration, R.array.sleep_duration)
            setSpinnerAdapter(qualityOfSleep, R.array.sleep_quality)
            setSpinnerAdapter(spinnerMinWorkingHours, R.array.working_hours)
            setSpinnerAdapter(spinnerMaksWorkingHours, R.array.working_hours)
            setSpinnerAdapter(bmi, R.array.dropdown_bmi)
            setSpinnerAdapter(heartRate, R.array.dropdown_heart_rate)
        }
    }

    private fun setOnItemSelectedListener(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                validateInputs()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No operation needed
            }
        }
    }

    private fun isAllFilled() {
        val gender = binding.etGender
        val age = binding.etAge
        val sleepDuration = binding.etSleepDuration
        val qualityOfSleep = binding.etSleepQuality
        val physicalActivityLevel = binding.etPhysicalActivity
        val minWorkingHours = binding.etMinWorkingHours
        val maxWorkingHours = binding.etWorkingHours
        val bmi = binding.etBMICategory
        val bloodPressure = binding.etBloodPressure
        val heartRate = binding.etHeartRate
        val dailySteps = binding.etDailySteps


        binding.btnEditAndSave.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No operation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }

            override fun afterTextChanged(s: Editable?) {
                // No operation needed
            }
        }

        physicalActivityLevel.addTextChangedListener(textWatcher)
        bloodPressure.addTextChangedListener(textWatcher)
        dailySteps.addTextChangedListener(textWatcher)

        binding.apply {
            setOnItemSelectedListener(gender)
            setOnItemSelectedListener(age)
            setOnItemSelectedListener(sleepDuration)
            setOnItemSelectedListener(qualityOfSleep)
            setOnItemSelectedListener(minWorkingHours)
            setOnItemSelectedListener(maxWorkingHours)
            setOnItemSelectedListener(bmi)
            setOnItemSelectedListener(heartRate)
        }
    }

    private fun validateInputs() {
        val gender = binding.etGender.selectedItemPosition != 0
        val age = binding.etAge.selectedItemPosition != 0
        val sleepDuration = binding.etSleepDuration.selectedItemPosition != 0
        val qualityOfSleep = binding.etSleepQuality.selectedItemPosition != 0
        val physicalActivityLevel = binding.etPhysicalActivity.text.toString()
        val minWorkingHours = binding.etMinWorkingHours.selectedItemPosition != 0
        val maxWorkingHours = binding.etWorkingHours.selectedItemPosition != 0
        val bmi = binding.etBMICategory.selectedItemPosition != 0
        val bloodPressure = binding.etBloodPressure.text.toString()
        val heartRate = binding.etHeartRate.selectedItemPosition != 0
        val dailySteps = binding.etDailySteps.text.toString()

        binding.btnEditAndSave.isEnabled = gender && age && sleepDuration && qualityOfSleep && physicalActivityLevel.isNotEmpty() && minWorkingHours && maxWorkingHours && bmi && bloodPressure.isNotEmpty() &&heartRate && dailySteps.isNotEmpty()
    }

    private fun updateStressLevel () {
        binding.btnEditAndSave.setOnClickListener {
            gender = binding.etGender.selectedItem.toString()
            age = binding.etAge.selectedItem.toString().toIntOrNull() ?: 0
            sleepDuration = binding.etSleepDuration.selectedItem.toString().toIntOrNull() ?: 0
            qualityOfSleep = binding.etSleepQuality.selectedItem.toString().toIntOrNull() ?: 0
            physicalActivityLevel = binding.etPhysicalActivity.text.toString().toIntOrNull() ?: 0
            minWorkingHours = binding.etMinWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
            maxWorkingHours = binding.etWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
            bmi = binding.etBMICategory.selectedItem.toString().takeIf { it != "Select an option" }?:"kosong"
            bloodPressure = binding.etBloodPressure.text.toString().takeIf { it.isNotBlank() }?:"kosong"
            heartRate = binding.etHeartRate.selectedItem.toString().takeIf { it != "Select an option" }?.toIntOrNull() ?: 0
            dailySteps = binding.etDailySteps.text.toString().takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0

            mainViewModel.getSession().observe(this){
                var token = it.token

                predictViewModel.predictStress(gender = gender!!, age = age!!, sleepDuration = sleepDuration!!, qualityOfSleep = qualityOfSleep!!, physicalActivityLevel = physicalActivityLevel!!, minWorkingHours = minWorkingHours!!, maxWorkingHours = maxWorkingHours!!, bmiCategory = bmi, bloodPressure = bloodPressure, heartRate = heartRate, dailySteps = dailySteps, token)
            }

        }
    }

}