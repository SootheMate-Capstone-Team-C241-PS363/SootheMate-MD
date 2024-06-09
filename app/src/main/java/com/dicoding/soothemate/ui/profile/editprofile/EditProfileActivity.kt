package com.dicoding.soothemate.ui.profile.editprofile

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityEditProfileBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.ProfileViewModel
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private val profileViewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState === null){
            mainViewModel.getSession().observe(this) { user ->
                var token = user.token
                profileViewModel.getDetailProfile(token)
            }

            profileViewModel.apiMessage.observe(this){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }

        profileViewModel.detailProfile.observe(this) { userDetail ->
            if (userDetail != null) {
                binding.usernameEdt.text = Editable.Factory.getInstance().newEditable(userDetail.name)
                binding.emailEdt.text = Editable.Factory.getInstance().newEditable(userDetail.email)
                binding.birthDate.text = Editable.Factory.getInstance().newEditable(userDetail.birthDate)
                val genderArray = resources.getStringArray(R.array.dropdown_gender)
                val genderIndex = genderArray.indexOf(userDetail.gender)
                if (genderIndex != -1) {
                    binding.genderEdt.setSelection(genderIndex)
                }
            }
        }

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        genderInit()
        showDatePickerDialog()
        updateUserInfo()
        exitPage()

        supportActionBar?.hide()
    }

    private fun updateUserInfo() {
        binding.saveBtn.setOnClickListener {
            val username = binding.usernameEdt.text.toString()
            val birthDate = binding.birthDate.text.toString()
            val selectedGender = binding.genderEdt.selectedItem.toString()

            mainViewModel.getSession().observe(this) { user ->
                val token = user.token
                profileViewModel.updateUserInfo(username, selectedGender, birthDate, token)
            }
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${(selectedMonth + 1).toString().padStart(2, '0')}-${selectedDay.toString().padStart(2, '0')}"
            binding.birthDate.setText(selectedDate)
        }, year, month, day)

        binding.birthDate.setOnClickListener{
            datePickerDialog.show()
        }
    }

    private fun exitPage() {
        binding.backBtn.setOnClickListener{
            finish()
        }
    }

    private fun genderInit() {
        binding.apply {
            val gender: Spinner = genderEdt
            setSpinnerAdapter(gender, R.array.dropdown_gender)
        }
    }

    private fun setSpinnerAdapter(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            this,
            arrayResId,
            R.layout.dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}