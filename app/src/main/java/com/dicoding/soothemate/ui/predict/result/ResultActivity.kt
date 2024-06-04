package com.dicoding.soothemate.ui.predict.result

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        // inisialisasi tracking dummy
        animateProgress(45f)
        animateTextViewChange(45)
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
}