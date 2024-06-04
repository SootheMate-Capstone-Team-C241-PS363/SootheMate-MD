package com.dicoding.soothemate.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.soothemate.MainActivity
import com.dicoding.soothemate.R
import com.dicoding.soothemate.data.pref.UserModel
import com.dicoding.soothemate.databinding.ActivityLoginBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginBtn.setOnClickListener {
            login()
        }

    }


    private fun login() {
        val email = binding.emailEdittext.text.toString().trim()
        val password = binding.passwordEdittext.text.toString()
        loginViewModel.login(email, password)
        loginViewModel.loginSuccess.observe(this) { success ->
            if (success == true) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else if (success == false) {
                Toast.makeText(this, "gagal login", Toast.LENGTH_LONG).show()
            }
        }
    }
}