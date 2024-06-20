package com.dicoding.soothemate.customviews

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R

class CustomSelectOption @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatSpinner(context, attrs) {

    init {
        setBackgroundResource(R.drawable.input_bg)
    }

    fun setCustomBackground(resourceId: Int) {
        val backgroundDrawable: Drawable? = ContextCompat.getDrawable(context, resourceId)
        this.background = backgroundDrawable
    }

    fun isValidForm(): Boolean {
        if (selectedItemPosition == 0) {
            val red: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_error)
            this.background = red
            return false
        } else {
            val blue: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg)
            this.background = blue
            return true
        }
    }

    fun isValidProfile(): Boolean {
        if (selectedItemPosition == 0) { 
            val red: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_white_error)
            this.background = red
            return false
        } else {
            val blue: Drawable? = ContextCompat.getDrawable(context, R.drawable.input_bg_white)
            this.background = blue
            return true
        }
    }
}