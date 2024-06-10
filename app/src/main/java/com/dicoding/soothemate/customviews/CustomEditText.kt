package com.dicoding.soothemate.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        hint = "Input"
        val initialBackground: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg)
        this.background = initialBackground

        setBackgroundResource(R.drawable.input_bg)
    }

    fun setCustomBackground(resourceId: Int) {
        val backgroundDrawable: Drawable? = ContextCompat.getDrawable(context, resourceId)
        this.background = backgroundDrawable
    }

    fun isValidForm(): Boolean {
        val text = text.toString()
        if (text.isNullOrEmpty()) {
            error = "this field cannot be empty"
            val red: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_error)
            this.background = red
            return false
        } else  {
            val blue: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg)
            this.background = blue
            return true
        }
    }

    fun isValidProfile(): Boolean {
        val text = text.toString()
        if (text.isNullOrEmpty()) {
            error = "this field cannot be empty"
            val red: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_white_error)
            this.background = red
            return false
        } else  {
            val blue: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_white)
            this.background = blue
            return true
        }
    }
}