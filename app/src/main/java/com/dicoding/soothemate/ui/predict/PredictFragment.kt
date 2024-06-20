package com.dicoding.soothemate.ui.predict

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CustomEditText
import com.dicoding.soothemate.customviews.CustomSelectOption
import com.dicoding.soothemate.databinding.FragmentPredictBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.bmi.BmiCalculateActivity
import com.dicoding.soothemate.ui.predict.result.ResultActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null

    private var token: String? = null

    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    var gender: String? = null
    var age: Int? = 0
    var sleepDuration: Int? = 0
    var sleepQuality: Int? = 0
    var physicalActivity: Int? = 0
    var minWorkingHours: Int? = 0
    var maksWorkingHours: Int? = 0
    var bmiCategory: String? = null
    var bloodPressure: String? = null
    var heartRate: Int? = null
    var dailySteps: Int? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState === null) {
            predictViewModel.apiMessage.observe(viewLifecycleOwner) {
                if (it.toString() == "All optional fields must be provided together: blood_pressure, heart_rate, daily_steps, bmi_category") {
                    Toast.makeText(
                        requireContext(),
                        "Please fill out all the necessary forms",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        changeEditTextBg(binding.predictLayout, R.drawable.input_bg)
        changeSpinnerBg(binding.predictLayout, R.drawable.input_bg)

        dropdownComponent()
        bmiCalculate()
        extras()
        calculate()


        return root
    }

    private fun setSpinnerAdapter(spinner: Spinner, arrayResId: Int) {
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(arrayResId)
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun dropdownComponent() {
        binding.apply {
            val spinnerGender: Spinner = spinnerGender
            val spinnerAge: Spinner = spinnerAge
            val spinnerSleepDuration: Spinner = spinnerSleepDuration
            val qualityOfSleep: Spinner = spinnerSleepQuality
            val spinnerMinWorkingHours: Spinner = spinnerMinWorkingHours
            val spinnerMaksWorkingHours: Spinner = spinnerMaksWorkingHours
            val bmi: Spinner = spinnerBmi
            val heartRate: Spinner = spinnerHeartRate

            setSpinnerAdapter(spinnerGender, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerAge, R.array.dropdown_age)
            setSpinnerAdapter(spinnerSleepDuration, R.array.sleep_duration)
            setSpinnerAdapter(qualityOfSleep, R.array.sleep_quality)
            setSpinnerAdapter(spinnerMinWorkingHours, R.array.working_hours)
            setSpinnerAdapter(spinnerMaksWorkingHours, R.array.working_hours)
            setSpinnerAdapter(bmi, R.array.dropdown_bmi)
            setSpinnerAdapter(heartRate, R.array.dropdown_heart_rate)
        }
    }

    private fun extras() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.extras.visibility = View.VISIBLE
            } else {
                binding.extras.visibility = View.GONE
            }
        }
    }

    private fun bmiCalculate() {
        binding.bmiCalculateBtn.setOnClickListener {
            startActivity(Intent(requireContext(), BmiCalculateActivity::class.java))
        }
    }

    private fun changeEditTextBg(viewGroup: ViewGroup, backgroundResId: Int) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is CustomEditText) {
                view.setCustomBackground(backgroundResId)
            }
        }
    }

    private fun changeSpinnerBg(viewGroup: ViewGroup, backgroundResId: Int) {
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is CustomSelectOption) {
                view.setCustomBackground(backgroundResId)
            }
        }
    }

    private fun calculate() {
        binding.calculateBtn.setOnClickListener {
            gender = binding.spinnerGender.selectedItem.toString()
            age = binding.spinnerAge.selectedItem.toString().toIntOrNull() ?: 0
            sleepDuration = binding.spinnerSleepDuration.selectedItem.toString().toIntOrNull() ?: 0
            sleepQuality = binding.spinnerSleepQuality.selectedItem.toString().toIntOrNull() ?: 0
            physicalActivity = binding.physicalActivity.text.toString().toIntOrNull() ?: 0
            minWorkingHours = binding.spinnerMinWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
            maksWorkingHours = binding.spinnerMaksWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
            bmiCategory = binding.spinnerBmi.selectedItem.toString().takeIf { it != "Select an option" }?:"kosong"
            bloodPressure = binding.bloodPressureEdtl.text.toString().takeIf { it.isNotBlank() }?:"kosong"
            heartRate = binding.spinnerHeartRate.selectedItem.toString().takeIf { it != "Select an option" }?.toIntOrNull() ?: 0
            dailySteps = binding.dailyStepsEdtl.text.toString().takeIf { it.isNotBlank() }?.toIntOrNull() ?: 0



            val isChecked = binding.checkBox.isChecked
            if (isChecked) {
                if (validateExtras()) {
                    if (token == null) {
                        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                            token = user.token

                        }
                    } else {
                        predictViewModel.predictStress(
                            gender!!,
                            age!!,
                            sleepDuration!!,
                            sleepQuality!!,
                            physicalActivity!!,
                            minWorkingHours!!, maksWorkingHours!!, bmiCategory, bloodPressure, heartRate, dailySteps, token!!)
                    }
                }
            } else {
                if (validateMandatory()){
                    if (token == null) {
                        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                            token = user.token

                        }
                    } else {
                        predictViewModel.predictStress(
                            gender!!,
                            age!!,
                            sleepDuration!!,
                            sleepQuality!!,
                            physicalActivity!!,
                            minWorkingHours!!, maksWorkingHours!!, null, null, null, null,
                            token!!
                        )
                    }
                }
            }
        }

        predictViewModel.stressValue.observe(viewLifecycleOwner) { stressLevel ->
            if (stressLevel != null) {
                Intent(requireContext(), ResultActivity::class.java).also {
                    it.putExtra(ResultActivity.STRESS_VALUE, stressLevel.stressLevel.toString())
                    it.putExtra(ResultActivity.STRESS_TITLE, stressLevel.title)
                    it.putExtra(ResultActivity.STRESS_DESC, stressLevel.description)
                    it.putExtra(ResultActivity.GENDER, gender.toString())
                    it.putExtra(ResultActivity.AGE, age.toString())
                    it.putExtra(ResultActivity.SLEEP_DURATION, sleepDuration.toString())
                    it.putExtra(ResultActivity.SLEEP_QUALITY, sleepQuality.toString())
                    it.putExtra(ResultActivity.PHYSICAL_ACTIVITY, physicalActivity.toString())
                    it.putExtra(ResultActivity.MIN_WORKING_HOURS, minWorkingHours.toString())
                    it.putExtra(ResultActivity.MAX_WORKING_HOURS, maksWorkingHours.toString())
                    it.putExtra(ResultActivity.BMI, bmiCategory.toString())
                    it.putExtra(ResultActivity.BLOOD_PRESSURE, bloodPressure.toString())
                    it.putExtra(ResultActivity.HEART_RATE, heartRate.toString())
                    it.putExtra(ResultActivity.STEPS, dailySteps.toString())


                    startActivity(it)
                }
            }
        }
    }

    private fun validateMandatory(): Boolean {
        val dropdownValue = listOf(
            binding.spinnerGender,
            binding.spinnerAge,
            binding.spinnerSleepDuration,
            binding.spinnerSleepQuality,
            binding.spinnerMinWorkingHours,
            binding.spinnerMaksWorkingHours
        )

        val editTexts = listOf(
            binding.physicalActivity
        )

        var allValid = true

        for (editText in editTexts) {
            if (!editText.isValidForm()) {
                allValid = false
            }
        }

        for (dropdown in dropdownValue) {
            if (!dropdown.isValidForm()) {
                allValid = false
            }
        }

        return allValid
    }

    private fun validateExtras(): Boolean {
        val editTexts = listOf(
            binding.physicalActivity,
            binding.dailyStepsEdtl,
            binding.bloodPressureEdtl
        )

        val dropdownValue = listOf(
            binding.spinnerGender,
            binding.spinnerAge,
            binding.spinnerSleepDuration,
            binding.spinnerSleepQuality,
            binding.spinnerMinWorkingHours,
            binding.spinnerMaksWorkingHours,
            binding.spinnerBmi,
            binding.spinnerHeartRate
        )

        var allValid = true

        for (editText in editTexts) {
            if (!editText.isValidForm()) {
                allValid = false
            }
        }

        for (dropdown in dropdownValue) {
            if (!dropdown.isValidForm()) {
                allValid = false
            }
        }
        return allValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}