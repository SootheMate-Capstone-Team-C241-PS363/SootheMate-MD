package com.dicoding.soothemate.ui.predict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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

    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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


        changeEditTextBg(binding.predictLayout, R.drawable.input_bg)
        changeSpinnerBg(binding.predictLayout, R.drawable.input_bg)

        dropdownComponent()
        bmiCalculate()
        extras()
        calculate()

        return root
    }

    private fun setSpinnerAdapter(spinner: Spinner, arrayResId: Int) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            arrayResId,
            android.R.layout.simple_spinner_dropdown_item
        )
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

            setSpinnerAdapter(spinnerGender, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerAge, R.array.dropdown_age)
            setSpinnerAdapter(spinnerSleepDuration, R.array.sleep_duration)
            setSpinnerAdapter(qualityOfSleep, R.array.sleep_quality)
            setSpinnerAdapter(spinnerMinWorkingHours, R.array.working_hours)
            setSpinnerAdapter(spinnerMaksWorkingHours, R.array.working_hours)
            setSpinnerAdapter(bmi, R.array.dropdown_gender)
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

    private fun bmiCalculate(){
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
            val gender = binding.spinnerGender.selectedItem.toString()
            val age = binding.spinnerAge.selectedItem.toString().toIntOrNull() ?: 0
            val sleepDuration = binding.spinnerSleepDuration.selectedItem.toString().toIntOrNull() ?: 0
            val sleepQuality = binding.spinnerSleepQuality.selectedItem.toString().toIntOrNull() ?: 0
            val physicalActivity = binding.physicalActivity.text.toString().toIntOrNull() ?: 0
            val minWorkingHours = binding.spinnerMinWorkingHours.selectedItem.toString().toIntOrNull() ?: 0
            val maksWorkingHours = binding.spinnerMaksWorkingHours.selectedItem.toString().toIntOrNull() ?: 0

            val isChecked = binding.checkBox.isChecked
            if (isChecked) {
                if (validateExtras()) {
                    startActivity(Intent(requireContext(), ResultActivity::class.java))
                } else{
                    // do something bingung
                }
            } else {
                if (validateMandatory()) {
                    mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
                        var token = user.token
                        predictViewModel.predictStress(gender, age, sleepDuration, sleepQuality, physicalActivity, minWorkingHours, maksWorkingHours, null, null, null, null, token)
                    }

                    predictViewModel.isSuccess.observe(viewLifecycleOwner){
                        if (it == true) {
                            predictViewModel.stressValue.observe(viewLifecycleOwner){ result ->
                                Intent(requireContext(), ResultActivity::class.java).also {
                                    it.putExtra(ResultActivity.STRESS_VALUE, result.toString())
                                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    startActivity(it)
                                }
                            }
                        }
                    }
                } else{
                    // do something bingung
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
            binding.heartRateEdtl,
            binding.bloodPressureEdtl
        )

        val dropdownValue = listOf(
            binding.spinnerGender,
            binding.spinnerAge,
            binding.spinnerSleepDuration,
            binding.spinnerSleepQuality,
            binding.spinnerMinWorkingHours,
            binding.spinnerMaksWorkingHours,
            binding.spinnerBmi
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