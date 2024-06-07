package com.dicoding.soothemate.ui.onboarding

import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityOnboardingBinding
import com.dicoding.soothemate.ui.login.LoginActivity
import com.dicoding.soothemate.ui.signup.SignUpActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            loginText.setOnClickListener {
                startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            }
            signUp.setOnClickListener {
                startActivity(Intent(this@OnboardingActivity, SignUpActivity::class.java))
            }
        }

        val onboardingTitle = listOf(binding.title1, binding.title2, binding.title3, binding.title4)
        onboardingTitle.forEach { applyGradient(it) }
    }

    private fun applyGradient(textView: TextView) {
        val width = textView.paint.measureText(textView.text.toString())
        val startColor = ContextCompat.getColor(this, R.color.blue_500)
        val endColor = ContextCompat.getColor(this, R.color.blue_400)
        val shader = LinearGradient(
            0f, 0f, width, textView.textSize,
            startColor, endColor,
            Shader.TileMode.CLAMP
        )
        textView.paint.shader = shader
    }
}