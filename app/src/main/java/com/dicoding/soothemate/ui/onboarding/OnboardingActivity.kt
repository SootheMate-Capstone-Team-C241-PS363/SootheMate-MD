package com.dicoding.soothemate.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}