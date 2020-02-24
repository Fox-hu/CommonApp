package com.silver.fox.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * 仿qq步数
 * @Author fox
 * @Date 2020/2/24 14:22
 */
class QQstepView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    var outPaint = Paint()
    var innerPaint = Paint()
    var textPaint = Paint()
    var outColor = Color.RED
    var innerColor = Color.BLUE
    var borderWidth = 8
    var stepTextSize = 20
    var stepTextColor = Color.BLUE
    var stepMax = 100
    var currentStep = 50
        @Synchronized set(value) {
            field = value
            invalidate()
        }

    init {
        val arrays =
            context.obtainStyledAttributes(attrs, R.styleable.view_QQstepView)
        arrays.apply {
            outColor = getColor(R.styleable.view_QQstepView_view_outerColor, outColor)
            innerColor = getColor(R.styleable.view_QQstepView_view_innerColor, innerColor)
            stepTextColor = getColor(R.styleable.view_QQstepView_view_stepTextColor, stepTextColor)
            borderWidth = getDimensionPixelSize(
                R.styleable.view_QQstepView_view_borderWidth,
                px2sp(borderWidth)
            )

            stepTextSize = getDimensionPixelSize(
                R.styleable.view_QQstepView_view_stepTextSize,
                px2sp(stepTextSize)
            )
            recycle()
        }

        innerPaint.apply {
            isAntiAlias = true
            strokeWidth = borderWidth.toFloat()
            color = innerColor
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE

        }
        outPaint.apply {
            isAntiAlias = true
            strokeWidth = borderWidth.toFloat()
            color = outColor
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE

        }
        textPaint.apply {
            isAntiAlias = true
            textSize = stepTextSize.toFloat()
            color = stepTextColor
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val min = width.coerceAtMost(height)
        setMeasuredDimension(min, min)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        //环绕弧形的矩形框 不能是从0开始 否则头部显示不全
        //为了使头部显示完全 环绕弧形的矩形框要小于真实的宽高
        val rectF = RectF(
            (borderWidth / 2).toFloat(),
            (borderWidth / 2).toFloat(),
            (width - borderWidth / 2).toFloat(),
            (height - borderWidth / 2).toFloat()
        )
        //1.画外圆环
        canvas?.drawArc(rectF, 135F, 270F, false, outPaint)

        //2.画内圆环 按照比例
        //注意 这个地方要转成float 如果是int的话是拿不到浮点数的
        val sweepAngle = currentStep.toFloat() / stepMax.toFloat()
        canvas?.drawArc(rectF, 135F, sweepAngle * 270F, false, innerPaint)

        //3.画文字
        val stepText = currentStep.toString()
        val rect = Rect()
        textPaint.getTextBounds(stepText, 0, stepText.length, rect)
        val dx = width / 2 - rect.width() / 2
        //baseline的求法如MyTextView
        val fontMetrics = textPaint.fontMetricsInt
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        val baseLine = height / 2 + dy
        canvas?.drawText(stepText, dx.toFloat(), baseLine.toFloat(), textPaint)
    }
}