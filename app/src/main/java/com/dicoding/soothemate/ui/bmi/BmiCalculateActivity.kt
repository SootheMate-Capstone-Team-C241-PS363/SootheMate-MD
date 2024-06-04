package com.dicoding.soothemate.ui.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R
import soup.neumorphism.NeumorphButton

class BmiCalculateActivity : AppCompatActivity() {

    private lateinit var maleButton: NeumorphButton
    private lateinit var femaleButton: NeumorphButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_calculate)

        supportActionBar?.hide()

        maleButton = findViewById(R.id.male_value)
        femaleButton = findViewById(R.id.female_value)

        maleButton.setOnClickListener {
            selectGender(maleButton, femaleButton)
        }

        femaleButton.setOnClickListener {
            selectGender(femaleButton, maleButton)
        }
    }

    private fun selectGender(selectedButton: NeumorphButton, unselectedButton: NeumorphButton) {
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_500))

        selectedButton.setShadowElevation(0f)
        selectedButton.setShadowColorDark(0)
        selectedButton.setShadowColorLight(0)

        unselectedButton.setShadowElevation(6f)
        unselectedButton.setTextColor(ContextCompat.getColor(this, R.color.black_400))
        unselectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_200))
        unselectedButton.setShadowColorDark(ContextCompat.getColor(this, R.color.blue_300))
        unselectedButton.setShadowColorLight(ContextCompat.getColor(this, R.color.blue_200))
    }
}