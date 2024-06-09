package com.dicoding.soothemate.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.dicoding.soothemate.R
import com.google.android.material.textfield.TextInputEditText
import soup.neumorphism.NeumorphCardView

class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var cardView: NeumorphCardView
    private lateinit var editText: TextInputEditText

    init {
        initViews(context)
    }

    private fun initViews(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.input_layout, this, true)
        cardView = view.findViewById(R.id.neumorph_card_view)

        editText = cardView.findViewById(R.id.text_input_edit_text)

        editText.addTextChangedListener {
            updateStrokeColor()
        }
    }

    fun updateStrokeColor() {
        if (editText.text.isNullOrEmpty()) {
            val red = resources.getColor(R.color.red, context.theme)
            cardView.setStrokeColor(ColorStateList.valueOf(red))
        } else {
            val blue400 = resources.getColor(R.color.blue_400, context.theme)
            cardView.setStrokeColor(ColorStateList.valueOf(blue400))
        }
    }
}