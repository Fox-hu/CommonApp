package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View


class MyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    var text: String = ""
    var textSize = 15
    var textColor = Color.BLACK
    var paint = Paint()

    init {
        val arr =
            context.obtainStyledAttributes(attrs, R.styleable.view_MyTextView)
        arr.apply {
            text = getString(R.styleable.view_MyTextView_view_myText)
            textColor = getColor(R.styleable.view_MyTextView_view_myTextColor, textColor)
            textSize =
                getDimensionPixelSize(
                    R.styleable.view_MyTextView_view_myTextSize,
                    px2sp(textSize)
                )
            recycle()
        }

        paint.apply {
            //开启抗锯齿
            isAntiAlias = true
            textSize = this@MyTextView.textSize.toFloat()
            color = textColor
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            width = bounds.width() + paddingStart + paddingEnd
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)
            height = bounds.height() + paddingTop + paddingBottom
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //y代表的是基线 baseLine
        //关于基线的内容https://blog.csdn.net/xude1985/article/details/51532949
        val fontMetricsInt = paint.fontMetricsInt
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        val baseLine = height / 2 + dy

        canvas?.drawText(text, paddingStart.toFloat(), baseLine.toFloat(), paint)
    }

}


