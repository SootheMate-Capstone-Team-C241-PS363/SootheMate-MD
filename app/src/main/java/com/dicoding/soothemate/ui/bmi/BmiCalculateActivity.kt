package com.dicoding.soothemate.ui.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.ActivityBmiCalculateBinding
import soup.neumorphism.NeumorphButton

class BmiCalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiCalculateBinding

    private lateinit var maleButton: NeumorphButton
    private lateinit var femaleButton: NeumorphButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

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

        getBmi()
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
    }

    private fun calculateBmi(height: Int, weight: Double) {
        val heightCalculate = height / 100.0
        val bmiCalculate = weight / (heightCalculate * heightCalculate)
        var result: String = ""
        var bmiDescription: String = ""

        if (bmiCalculate < 18.5) {
            result = "Underweight"
            bmiDescription = "gajgkjnajaejeajbgakjvjaenioga"
        } else if (bmiCalculate >= 18.5 && bmiCalculate < 24.9) {
            result = "Normal weight"
            bmiDescription = "gajgkjnajaejeajbgakjvjaenioga"
        } else if (bmiCalculate >= 24.9 && bmiCalculate < 29.9) {
            result = "Overweight"
            bmiDescription = "gajgkjnajaejeajbgakjvjaenioga"
        } else if (bmiCalculate >= 29.9) {
            result = "Obesity"
            bmiDescription = "gajgkjnajaejeajbgakjvjaenioga"
        }

        binding.bmiEdt.gravity = android.view.Gravity.TOP
        val formattedBMI = String.format("%.1f", bmiCalculate.toFloat())
        val combinedText = "$formattedBMI ($result) " +
                "$bmiDescription"

        val spannableString = SpannableString(combinedText)

        val startIndex = combinedText.indexOf("$bmiDescription")
        val endIndex = combinedText.length
        spannableString.setSpan(AbsoluteSizeSpan(13, true), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.bmiEdt.text = Editable.Factory.getInstance().newEditable(spannableString)
    }

    private fun getBmi(){
        binding.calculateBtn.setOnClickListener {
            val height = binding.heightEdt.text.toString().toInt()
            val weight = binding.weightEdt.text.toString().toDouble()
            calculateBmi(height, weight)
        }
    }
}