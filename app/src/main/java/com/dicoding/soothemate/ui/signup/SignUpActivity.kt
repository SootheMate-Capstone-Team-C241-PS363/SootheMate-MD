package com.dicoding.soothemate.ui.signup

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.soothemate.MainActivity
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivitySignUpBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.SignUpViewModel
import java.util.Calendar

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding

    private val signUpViewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.birthDateEdittext.setOnClickListener {
            showDatePickerDialog()
        }

        binding.registerBtn.setOnClickListener {
            Register()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "${selectedDay.toString().padStart(2, '0')}-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedYear}"
            binding.birthDateEdittext.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun Register() {
        val name = binding.usernameEdittext.text.toString()
        val email = binding.emailEdittext.text.toString().trim()
        val gender = binding.genderEdittext.text.toString().trim()
        val birthDate = binding.birthDateEdittext.text.toString().trim()
        val password = binding.passwordEdittext.text.toString()

        signUpViewModel.signUp(name, email, password, gender, birthDate)
        signUpViewModel.signUpSuccess.observe(this) { success ->
            if (success == true) {
                val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else if (success == false) {
                Toast.makeText(this, "Gagal Register", Toast.LENGTH_SHORT).show()
            }
        }
    }
}