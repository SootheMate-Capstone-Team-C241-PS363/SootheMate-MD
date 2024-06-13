package com.dicoding.soothemate.ui.predict.result

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CircularProgressView
import com.dicoding.soothemate.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    private lateinit var circularProgressView: CircularProgressView
    private var currentProgress: Float = 0f
    private var currentStress: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResultBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        circularProgressView = binding.circularProgressView

        val stressValue = intent.getStringExtra(STRESS_VALUE)

        if (stressValue != null) {
            animateProgress(stressValue.toFloat())
        }
        if (stressValue != null) {
            animateTextViewChange(stressValue.toInt())
        }
    }

    // animate tracking indicator
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

    // animate angka
    private fun animateTextViewChange(targetProgress: Int) {
        val animator = ValueAnimator.ofInt(currentStress, targetProgress)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            binding.stressValue.text = "$animatedValue%"
        }
        animator.start()

        currentStress = targetProgress
    }

    companion object {
        var STRESS_VALUE = "stress_value"
    }
}