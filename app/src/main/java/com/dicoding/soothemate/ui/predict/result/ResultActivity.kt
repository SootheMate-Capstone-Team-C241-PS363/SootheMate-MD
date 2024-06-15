package com.dicoding.soothemate.ui.predict.result

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.dicoding.soothemate.MainActivity
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CircularProgressView
import com.dicoding.soothemate.databinding.ActivityResultBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel

class ResultActivity : AppCompatActivity() {

    private val predictViewModel by viewModels<PredictViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    var stressValue: Int = 0
    var stressTitle: String = ""
    var stressDesc: String = ""
    var ageValue: Int = 0
    var genderValue: String = ""
    var minWorkingHours: Int = 0
    var maksWorkingHours: Int = 0
    var physicalActivity: Int = 0
    var sleepQuality: Int = 0
    var sleepDuration: Int = 0

    var token: String? = null

    private lateinit var binding: ActivityResultBinding

    private lateinit var circularProgressView: CircularProgressView
    private var currentProgress: Float = 0f
    private var currentStress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        predictViewModel.isSuccess.observe(this){
            if (it == true){
                Toast.makeText(this, "Save predict berhasil", Toast.LENGTH_LONG).show()
                exitPage()
            } else {
                Toast.makeText(this, "Save predict gagal", Toast.LENGTH_LONG).show()
            }
        }

        circularProgressView = binding.circularProgressView

        stressValue = intent.getStringExtra(STRESS_VALUE).toString().toInt()
        stressTitle = intent.getStringExtra(STRESS_TITLE).toString()
        stressDesc = intent.getStringExtra(STRESS_DESC).toString()
        ageValue = intent.getStringExtra(AGE).toString().toInt()
        genderValue = intent.getStringExtra(GENDER).toString()
        minWorkingHours = intent.getStringExtra(MIN_WORKING_HOURS).toString().toInt()
        maksWorkingHours = intent.getStringExtra(MAX_WORKING_HOURS).toString().toInt()
        physicalActivity = intent.getStringExtra(PHYSICAL_ACTIVITY).toString().toInt()
        sleepQuality = intent.getStringExtra(SLEEP_QUALITY).toString().toInt()
        sleepDuration = intent.getStringExtra(SLEEP_DURATION).toString().toInt()

        animateProgress(stressValue!!.toFloat())
        animateTextViewChange(stressValue!!.toInt())

        savePredictResult()
        exitWithoutSave()
    }

    private fun animateProgress(targetProgress: Float) {
        val animator = ValueAnimator.ofFloat(currentProgress, targetProgress)
        animator.duration = 1000
        animator.addUpdateListener { valueAnimator ->
            val progress = valueAnimator.animatedValue as Float
            circularProgressView.setProgress(progress)
        }
        animator.start()

        currentProgress = targetProgress
    }

    private fun animateTextViewChange(targetProgress: Int) {
        val animator = ValueAnimator.ofInt(currentStress, targetProgress)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            binding.stressValue.text = "$animatedValue%"
            binding.stressTitle.text = stressTitle
            binding.stressDescription.text = stressDesc
        }
        animator.start()

        currentStress = targetProgress
    }

    private fun savePredictResult() {
        binding.saveBtn.setOnClickListener {
            if (token == null){
                mainViewModel.getSession().observe(this){
                    token = it.token
                    predictViewModel.savePredictStress(genderValue, ageValue, sleepDuration, sleepQuality, physicalActivity, minWorkingHours, maksWorkingHours, stressValue, stressTitle, stressDesc,
                        token!!
                    )
                }
            }
        }
    }

    private fun exitPage() {
        val intent = Intent(this@ResultActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun exitWithoutSave() {
        binding.dontSaveBtn.setOnClickListener {
            exitPage()
        }
    }

    companion object {
        var STRESS_VALUE = "stress_value"
        var STRESS_TITLE = "stress_title"
        var STRESS_DESC = "stress_desc"
        var AGE = "age"
        var GENDER = "gender"
        var MAX_WORKING_HOURS = "max_working_hours"
        var MIN_WORKING_HOURS = "min_working_hours"
        var PHYSICAL_ACTIVITY = "physical_activity"
        var SLEEP_QUALITY = "sleep_quality"
        var SLEEP_DURATION = "sleep_duration"
    }
}