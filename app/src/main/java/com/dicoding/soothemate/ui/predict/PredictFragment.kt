package com.dicoding.soothemate.ui.predict

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.soothemate.R
import com.dicoding.soothemate.databinding.FragmentPredictBinding
import com.dicoding.soothemate.ui.bmi.BmiCalculateActivity
import com.dicoding.soothemate.ui.predict.result.ResultActivity
import com.dicoding.soothemate.viewmodel.PredictViewModel

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val predictViewModel =
//            ViewModelProvider(this).get(PredictViewModel::class.java)

        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.apply {
            val spinnerGender: Spinner = spinnerGender
            val spinnerAge: Spinner = spinnerAge
            val spinnerSleepDuration: Spinner = spinnerSleepDuration
            val spinnerPhysicalActivityLevel: Spinner = spinnerPhysicalActivity
            val spinnerWorkingHours: Spinner = spinnerWorkingHours
            val bmi: Spinner = spinnerBmi

            setSpinnerAdapter(spinnerGender, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerAge, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerSleepDuration, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerPhysicalActivityLevel, R.array.dropdown_gender)
            setSpinnerAdapter(spinnerWorkingHours, R.array.dropdown_gender)
            setSpinnerAdapter(bmi, R.array.dropdown_gender)
        }

        extras()
        bmiCalculate()
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

    private fun calculate() {
        binding.calculateBtn.setOnClickListener {
            startActivity(Intent(requireContext(), ResultActivity::class.java))
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