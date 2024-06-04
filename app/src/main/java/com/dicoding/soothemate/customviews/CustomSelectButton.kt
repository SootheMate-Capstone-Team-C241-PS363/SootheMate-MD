package com.dicoding.soothemate.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R

class CustomSelectButton : AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var enabledtxtColor: Int = 0
    private var disabledtxtColor: Int = 1
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable
    init {
        enabledtxtColor = ContextCompat.getColor(context, android.R.color.background_light)
        disabledtxtColor = ContextCompat.getColor(context, R.color.black_400)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.button_disable) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(if (isEnabled) enabledtxtColor else disabledtxtColor)
        setTypeface(typeface, Typeface.BOLD)
        gravity = Gravity.CENTER
    }
}