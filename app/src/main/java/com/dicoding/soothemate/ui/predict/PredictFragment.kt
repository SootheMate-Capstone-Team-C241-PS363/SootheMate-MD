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
import androidx.lifecycle.ViewModelProvider
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CustomEditText
import com.dicoding.soothemate.databinding.FragmentPredictBinding
import com.dicoding.soothemate.factory.ViewModelFactory
import com.dicoding.soothemate.ui.bmi.BmiCalculateActivity
import com.dicoding.soothemate.ui.predict.result.ResultActivity
import com.dicoding.soothemate.viewmodel.MainViewModel
import com.dicoding.soothemate.viewmodel.PredictViewModel
import com.google.android.material.textfield.TextInputEditText

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null

    private val predictViewModel: PredictViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
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
            R.layout.dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.dropdown_item)
        spinner.adapter = adapter
    }

    private fun dropdownComponent() {
        binding.apply {
            val spinnerGender: Spinner = spinnerGender.findViewById(R.id.spinner)
            val spinnerAge: Spinner = spinnerAge.findViewById(R.id.spinner)
            val spinnerSleepDuration: Spinner = spinnerSleepDuration.findViewById(R.id.spinner)
            val spinnerPhysicalActivityLevel: Spinner = spinnerPhysicalActivity.findViewById(R.id.spinner)
            val spinnerWorkingHours: Spinner = spinnerWorkingHours.findViewById(R.id.spinner)
            val bmi: Spinner = spinnerBmi.findViewById(R.id.spinner)

            setSpinnerAdapter(spinnerGender, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerAge, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerSleepDuration, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerPhysicalActivityLevel, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerWorkingHours, R.array.dropdown_gender)
            setSpinnerAdapter(bmi, R.array.dropdown_gender)
        }
    }



    private fun calculate() {
        binding.calculateBtn.setOnClickListener {

            val isChecked = binding.checkBox.isChecked

            if (isChecked) {
                val edtValue = listOf(
                    binding.dailyStepsEdtl,
                    binding.heartRateEdtl,
                    binding.bloodPressureEdtl
                )

                val dropdownItem = listOf(
                    binding.spinnerGender,
                    binding.spinnerAge,
                    binding.spinnerSleepDuration,
                    binding.spinnerPhysicalActivity,
                    binding.spinnerWorkingHours,
                    binding.spinnerBmi
                )

                if (predictViewModel.isFieldEmpty(edtValue, dropdownItem)) {
                    startActivity(Intent(requireContext(), ResultActivity::class.java))
                }
            } else {
                val dropdownItem = listOf(
                    binding.spinnerGender,
                    binding.spinnerAge,
                    binding.spinnerSleepDuration,
                    binding.spinnerPhysicalActivity,
                    binding.spinnerWorkingHours
                )

                if (predictViewModel.isFieldEmpty(null, dropdownItem)) {
                    startActivity(Intent(requireContext(), ResultActivity::class.java))
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}