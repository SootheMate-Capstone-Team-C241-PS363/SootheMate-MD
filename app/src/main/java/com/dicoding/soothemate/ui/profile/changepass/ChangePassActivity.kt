package com.dicoding.soothemate.ui.profile.changepass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityChangePassBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel

class ChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePassBinding

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        validationPass()
    }

    private fun setup() {
        binding.confirmButton.setOnClickListener {
            mainViewModel.getSession().observe(this) { user ->
                val token = user.token
                var oldPassword = binding.oldPasswordEdt.text.toString()
                var newPassword = binding.newPasswordEdt.text.toString()
                var passwordConfirmation = binding.confirmPasswordEdt.text.toString()
                profileViewModel.changePassword(oldPassword, newPassword, passwordConfirmation, token)
            }

            profileViewModel.isSuccess.observe(this){ success ->
                if (success == true) {
                    Toast.makeText(this, "Password berhasil diperbarui", Toast.LENGTH_LONG).show()
                    customAlertDialog()
                } else {
                    Toast.makeText(this, "Password gagal diperbarui", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun customAlertDialog() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.custom_dialog, null)
        val button = view.findViewById<TextView>(R.id.continue_btn)
        builder.setView(view)
        button.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(true)

        builder.setOnShowListener {
            val window = builder.window
            window?.setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        builder.show()
    }

    private fun validationPass() {
        val oldPasswordEditText = binding.oldPasswordEdt
        val newPasswordEditText = binding.newPasswordEdt
        val confirmPasswordEditText = binding.confirmPasswordEdt
        val confirmButton = binding.confirmButton

        confirmButton.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                confirmButton.isEnabled = !confirmPasswordEditText.text.isNullOrEmpty() &&
                        !oldPasswordEditText.text.isNullOrEmpty() &&  !newPasswordEditText.text.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }
        }

        confirmPasswordEditText.addTextChangedListener(textWatcher)
        oldPasswordEditText.addTextChangedListener(textWatcher)
        newPasswordEditText.addTextChangedListener(textWatcher)

        confirmButton.setOnClickListener {
            setup()
        }
    }

}