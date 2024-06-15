package com.dicoding.soothemate.ui.history

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityHistoryDetailBinding

class HistoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initSpinner()
        initView()
        initTextWatcher()
    }

    private fun initTextWatcher() {
        binding.etAge.addTextChangedListener {
            checkField()
        }

        binding.etSleepDuration.addTextChangedListener {
            checkField()
        }

        binding.etSleepQuality.addTextChangedListener {
            checkField()
        }

        binding.etPhysicalActivity.addTextChangedListener {
            checkField()
        }

        binding.etWorkingHours.addTextChangedListener {
            checkField()
        }

        binding.etBloodPressure.addTextChangedListener {
            checkField()
        }

        binding.etHeartRate.addTextChangedListener {
            checkField()
        }

        binding.etDailySteps.addTextChangedListener {
            checkField()
        }
    }

    private fun initView() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.btnBackToHistory.setOnClickListener { finish() }

        binding.btnEditAndSave.setOnClickListener {
            val intent = Intent(this@HistoryDetailActivity, HistoryActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun initSpinner() {
        val spinner: Spinner = binding.etGender

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_gender,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val spinnerBMI: Spinner = binding.etBMICategory

        ArrayAdapter.createFromResource(
            this,
            R.array.dropdown_bmi,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerBMI.adapter = adapter
        }


    }

    private fun checkField(){
        if(!binding.etAge.text.toString().isNullOrEmpty() &&
            !binding.etSleepDuration.text.toString().isNullOrEmpty() &&
            !binding.etSleepQuality.text.toString().isNullOrEmpty() &&
            !binding.etPhysicalActivity.text.toString().isNullOrEmpty() &&
            !binding.etWorkingHours.text.toString().isNullOrEmpty() &&
            !binding.etBloodPressure.text.toString().isNullOrEmpty() &&
            !binding.etHeartRate.text.toString().isNullOrEmpty() &&
            !binding.etDailySteps.text.toString().isNullOrEmpty()){
            binding.btnEditAndSave.isEnabled = true
        }else{
            binding.btnEditAndSave.isEnabled = false
        }
    }
}