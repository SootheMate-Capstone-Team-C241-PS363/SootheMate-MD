package com.dicoding.soothemate.ui.login

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityLoginBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.signup.SignUpActivity
import com.dicoding.soothemate.ui.welcome.WelcomeActivity
import com.dicoding.soothemate.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.toSignUp.setOnClickListener {
            val intentToSignUp = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intentToSignUp)
        }

        val paint = binding.tvGetStarted.paint
        val width = paint.measureText(binding.tvGetStarted.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, binding.tvGetStarted.textSize,
            intArrayOf(
                Color.parseColor("#800098DA"), Color.parseColor("#5DCCFC"), Color.parseColor("#0098DA")
            ), null, Shader.TileMode.REPEAT
        )
        binding.tvGetStarted.paint.shader = textShader
    }

    private fun initViewModel() {
        loginViewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        loginViewModel.loginSuccess.observe(this, Observer { loginSuccess ->
            loginSuccess?.let {
                if (it) {
                    val intentToWelcome = Intent(this@LoginActivity, WelcomeActivity::class.java)
                    intentToWelcome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intentToWelcome)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}