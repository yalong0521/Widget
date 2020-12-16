package com.larkin.view.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mRate: Int
    private var mOuterColor: Int
    private var mInnerColor: Int
    private val mPaint: Paint = Paint()
    private var mOuterRectF = RectF()
    private var mInnerRectF = RectF()
    private var mOuterStartAngle = 0f
    private var mInnerStartAngle = 20f
    private var mAroundAnimSwitch = false

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LoadingView)
        mRate = a.getInteger(R.styleable.LoadingView_lvRate, 2)
        mOuterColor = a.getColor(R.styleable.LoadingView_lvOuterColor, Color.parseColor("#0A6BBD"))
        mInnerColor = a.getColor(R.styleable.LoadingView_lvInnerColor, Color.parseColor("#3E9AE8"))
        a.recycle()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.color = mOuterColor
    }

    private fun startAroundAnim() {
        mAroundAnimSwitch = true
        aroundAnim()
    }

    private fun stopAroundAnim() {
        mAroundAnimSwitch = false
    }

    private fun aroundAnim() {
        mOuterStartAngle += mRate
        if (mOuterStartAngle > 360) mOuterStartAngle %= 360
        mInnerStartAngle -= mRate
        if (mInnerStartAngle < -360) mInnerStartAngle %= 360
        postInvalidate()
        if (mAroundAnimSwitch) {
            postDelayed({ aroundAnim() }, 10)
        }
    }

    fun setRate(rate: Int) {
        this.mRate = rate
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val defaultSize = (resources.displayMetrics.density * 100).toInt()

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = defaultSize + paddingLeft + paddingRight
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = defaultSize + paddingTop + paddingBottom
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val availableWidth = measuredWidth - paddingLeft - paddingRight
        val availableHeight = measuredHeight - paddingTop - paddingBottom
        var mOuterDiameter = availableWidth.coerceAtMost(availableHeight).toFloat()
        var mInnerDiameter = mOuterDiameter * 3 / 4
        mPaint.strokeWidth = mOuterDiameter / 20f

        mOuterDiameter -= mPaint.strokeWidth
        mInnerDiameter -= mPaint.strokeWidth

        val outerLeft = (measuredWidth - mOuterDiameter) / 2f
        val outerTop = (measuredHeight - mOuterDiameter) / 2f
        val outerRight = outerLeft + mOuterDiameter
        val outerBottom = (outerTop + mOuterDiameter)
        mOuterRectF.set(outerLeft, outerTop, outerRight, outerBottom)

        val innerLeft = (measuredWidth - mInnerDiameter) / 2f
        val innerTop = (measuredHeight - mInnerDiameter) / 2f
        val innerRight = innerLeft + mInnerDiameter
        val innerBottom = (innerTop + mInnerDiameter)
        mInnerRectF.set(innerLeft, innerTop, innerRight, innerBottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.color = mOuterColor
        canvas?.drawArc(mOuterRectF, mOuterStartAngle, 150f, false, mPaint)
        canvas?.drawArc(mOuterRectF, mOuterStartAngle + 180f, 150f, false, mPaint)

        mPaint.color = mInnerColor
        canvas?.drawArc(mInnerRectF, mInnerStartAngle, 150f, false, mPaint)
        canvas?.drawArc(mInnerRectF, mInnerStartAngle + 180f, 150f, false, mPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAroundAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAroundAnim()
    }
}