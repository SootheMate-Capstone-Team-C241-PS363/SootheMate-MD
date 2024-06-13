package com.dicoding.soothemate.ui.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.dicoding.soothemate.databinding.ActivitySignUpBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.login.LoginActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.toLogIn.setOnClickListener {
            toLogin()
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etSetPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validateInput(username, email, password, confirmPassword)) {
                viewModel.signUp(username, email, password, confirmPassword,"", "")
                viewModel.signUpSuccess.observe(this) { isSignUpSuccess ->
                    if (isSignUpSuccess == true) {
                        toLogin()
                    }
                }
            }
        }
        val paint = binding.tvSignUp.paint
        val width = paint.measureText(binding.tvSignUp.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, binding.tvSignUp.textSize,
            intArrayOf(
                Color.parseColor("#800098DA"), Color.parseColor("#5DCCFC"), Color.parseColor("#0098DA")
            ), null, Shader.TileMode.REPEAT)

        binding.tvSignUp.paint.shader = textShader
    }

    private fun validateInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        var isValid = true
        if (username.isEmpty()) {
            binding.etUsernameLayout.error = "Username is required"
            isValid = false
        } else {
            binding.etUsernameLayout.error = null
        }

        if (email.isEmpty()) {
            binding.etEmailLayout.error = "Email is required"
            isValid = false
        } else {
            binding.etEmailLayout.error = null
        }

        if (password.isEmpty()) {
            binding.etSetPasswordLayout.error = "Password is required"
            isValid = false
        } else {
            binding.etSetPasswordLayout.error = null
        }

        if (password != confirmPassword) {
            binding.etConfirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        } else {
            binding.etConfirmPasswordLayout.error = null
        }

        return isValid
    }


    private fun toLogin() {
        val intentToLogin = Intent(this@SignUpActivity, LoginActivity::class.java)
        intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentToLogin)
        finish()
    }
}