package com.dicoding.soothemate.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.size
import androidx.core.widget.addTextChangedListener
import com.dicoding.soothemate.R
import com.google.android.material.textfield.TextInputEditText
import soup.neumorphism.NeumorphCardView

class CustomSelectOption @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){

    private lateinit var cardView: NeumorphCardView
    private lateinit var spinner: Spinner

    init {
        initViews(context)
    }

    private fun initViews(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.spinner_layout, this, true)
        cardView = view.findViewById(R.id.neumorph_card_view)
        spinner = view.findViewById(R.id.spinner)
    }

    fun updateStrokeColor() {
        if (spinner.selectedItem == null || spinner.selectedItemPosition == 0) {
            val red = resources.getColor(R.color.red, context.theme)
            cardView.setStrokeColor(ColorStateList.valueOf(red))
        } else {
            val blue400 = resources.getColor(R.color.blue_400, context.theme)
            cardView.setStrokeColor(ColorStateList.valueOf(blue400))
        }
    }
}