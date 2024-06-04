package com.dicoding.soothemate.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.dicoding.soothemate.R

class CircularProgressView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRectF: RectF = RectF()
    private var mStrokeWidth: Float = 50f
    private var mProgress: Float = 0f
    private var mMaxProgress: Float = 100f
    private var mStartAngle: Float = -90f

    private var mAnimator: ValueAnimator

    init {
        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth
        mPaint.strokeCap = Paint.Cap.ROUND

        mAnimator = ValueAnimator.ofFloat(0f, 360f)
        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.duration = 1000
        mAnimator.addUpdateListener {
            invalidate()
        }
        mAnimator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
        val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom

        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY) - mStrokeWidth / 2

        mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPaint.color = ContextCompat.getColor(context, R.color.blue_400)
        canvas.drawArc(mRectF, 0f, 360f, false, mPaint)

        mPaint.color = ContextCompat.getColor(context, R.color.blue_500)
        canvas.drawArc(mRectF, mStartAngle, 360 * mProgress / mMaxProgress, false, mPaint)
    }

    fun setProgress(progress: Float) {
        mProgress = progress
        invalidate()
    }

    override fun onDetachedFromWindow() {
        mAnimator.cancel()
        super.onDetachedFromWindow()
    }
}