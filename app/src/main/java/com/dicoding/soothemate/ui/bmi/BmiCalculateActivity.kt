package com.dicoding.soothemate.ui.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityBmiCalculateBinding
import com.dicoding.soothemate.utils.Utils
import soup.neumorphism.NeumorphButton

class BmiCalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiCalculateBinding

    private lateinit var maleButton: NeumorphButton
    private lateinit var femaleButton: NeumorphButton
    private var selectedGenderButton: NeumorphButton? = null
    private lateinit var utils : Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        utils = Utils()

        utils.setTransparentStatusBar(this)

        binding.apply {

            maleButton = maleValue
            femaleButton = femaleValue

            maleButton.setOnClickListener {
                selectGender(maleButton, femaleButton)
            }
            femaleButton.setOnClickListener {
                selectGender(femaleButton, maleButton)
            }
        }

        setupCalculateButton()
    }

    private fun validation(): Boolean{
        val editTexts = listOf(
            binding.ageEdt,
            binding.heightEdt,
            binding.weightEdt,
        )

        var allValid = true

        for (editText in editTexts) {
            if (!editText.isValidForm()) {
                allValid = false
            }
        }

        if (selectedGenderButton == null) {
            allValid = false
            Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
        }

        return allValid
    }

    private fun selectGender(selectedButton: NeumorphButton, unselectedButton: NeumorphButton) {
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_500))

        selectedButton.setShadowElevation(0f)
        selectedButton.setShadowColorDark(0)
        selectedButton.setShadowColorLight(0)

        unselectedButton.setShadowElevation(6f)
        unselectedButton.setTextColor(ContextCompat.getColor(this, R.color.black_two))
        unselectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_200))
        unselectedButton.setShadowColorDark(ContextCompat.getColor(this, R.color.blue_300))
        unselectedButton.setShadowColorLight(ContextCompat.getColor(this, R.color.blue_200))

        selectedGenderButton = selectedButton
    }

    private fun calculateBmi(height: Int, weight: Double) {
        val heightInMeters = height / 100.0
        val bmi = weight / (heightInMeters * heightInMeters)
        val result: String
        val bmiDescription: String

        when {
            bmi < 18.5 -> {
                result = "Underweight"
                bmiDescription = "You are underweight. Consider eating more and working on gaining muscle mass."
            }
            bmi < 24.9 -> {
                result = "Normal weight"
                bmiDescription = "You have a normal weight. Keep up the good work!"
            }
            bmi < 29.9 -> {
                result = "Overweight"
                bmiDescription = "You are overweight. Consider adopting a healthier diet and increasing physical activity."
            }
            else -> {
                result = "Obesity"
                bmiDescription = "You are obese. Consult with a healthcare provider for personalized advice."
            }
        }

        binding.bmiEdt.gravity = Gravity.TOP
        val formattedBMI = String.format("%.1f", bmi)
        val combinedText = "$formattedBMI \" $result\\n\\n\" $bmiDescription"

        val spannableString = SpannableString(combinedText)
        val startIndex = combinedText.indexOf(bmiDescription)
        val endIndex = combinedText.length
        spannableString.setSpan(AbsoluteSizeSpan(13, true), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.bmiEdt.text = Editable.Factory.getInstance().newEditable(spannableString)
    }

    private fun setupCalculateButton() {
        binding.calculateBtn.setOnClickListener {
            val height = binding.heightEdt.text.toString().toIntOrNull()
            val weight = binding.weightEdt.text.toString().toDoubleOrNull()

            if (height != null && weight != null) {
                if (validation()){
                    calculateBmi(height, weight)
                }
            } else {
                validation()
                binding.bmiEdt.text = Editable.Factory.getInstance().newEditable("Please enter valid height and weight.")
            }
        }
    }
}