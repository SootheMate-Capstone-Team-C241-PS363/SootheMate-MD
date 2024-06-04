package com.dicoding.soothemate.customviews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.dicoding.soothemate.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        hint = resources.getString(R.string.password_hint)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        if (text.isNullOrEmpty()) {
            error = null
        } else if (s.toString().length < MIN_PASSWORD_LENGTH) {
            error = "Password tidak boleh kurang dari ${MIN_PASSWORD_LENGTH} karakter"
        } else {
            error = null
        }
    }

    fun isValidPassword(): Boolean {
        val text = text.toString()
        return text.length >= MIN_PASSWORD_LENGTH
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}