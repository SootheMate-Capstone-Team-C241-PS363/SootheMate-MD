package com.dicoding.soothemate.viewmodel

import android.widget.Spinner
import androidx.core.view.size
import androidx.lifecycle.ViewModel
import com.dicoding.soothemate.R
import com.dicoding.soothemate.customviews.CustomEditText
import com.dicoding.soothemate.customviews.CustomSelectOption
import com.google.android.material.textfield.TextInputEditText

class PredictViewModel : ViewModel() {

    private var isEmpty: Boolean? = null

    fun isFieldEmpty(customEditTexts: List<CustomEditText?>?, customSpinners: List<CustomSelectOption?>?): Boolean {
        var isEmpty = true

        customEditTexts?.forEach { customEditText ->
            customEditText?.let {
                it.updateStrokeColor()
                val editText = it.findViewById<TextInputEditText>(R.id.text_input_edit_text)

                if (editText.text.isNullOrEmpty()) {
                    editText.error = "This field cannot be empty"
                    isEmpty = false
                }
            }
        }

        customSpinners?.forEach { customSpinner ->
            customSpinner?.let {
                it.updateStrokeColor()
                val spinner = it.findViewById<Spinner>(R.id.spinner)

                if (spinner.selectedItem == null || spinner.selectedItemPosition == 0) {
                    isEmpty = false
                }
            }
        }
        return isEmpty
    }
}