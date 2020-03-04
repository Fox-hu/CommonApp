package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 字母索引表
 * @Author fox
 * @Date 2020/3/3 13:17
 */
class LetterSideBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()
    private val highLightPaint = Paint()
    private var textSize = 20
    private var textColor = Color.RED
    private var itemHeight = 0
    private var currentTouchIndex: Int = -1
        set(value) {
            field = value
            invalidate()
        }

    private val LETTERS = arrayOf<String>("A", "B", "C", "D", "E")

    init {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.view_LetterSideBar)
        array.apply {
            textColor =
                getColor(R.styleable.view_LetterSideBar_view_textColor, textColor)
            textSize =
                getDimensionPixelSize(R.styleable.view_LetterSideBar_view_textSize, px2sp(textSize))
        }

        paint.config(textColor, textSize.toFloat())
        highLightPaint.config(Color.BLUE, textSize.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //高度是默认高度
        val height = MeasureSpec.getSize(heightMeasureSpec)

        //宽度是左右padding+字母宽度（取决于画笔）
        val textWidth = paint.measureText("A")
        val width = paddingLeft + paddingRight + textWidth
        setMeasuredDimension(width.toInt(), height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            //画26个字母
            //每个item的高度
            itemHeight = (height - paddingTop - paddingBottom) / LETTERS.size
            LETTERS.forEachIndexed { index, s ->
                //算出基线 1.算出字母中心位置 2.根据字母中心位置算出基线
                val letterCenterY = index * itemHeight + itemHeight / 2 + paddingTop
                val fontMetrics = paint.fontMetricsInt
                val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
                val baseLine = letterCenterY + dy
                //文字绘制在中间 宽度 - 文字的一半
                val offsetX = width / 2 - paint.measureText(s) / 2
                //如果是触摸的item则变色
                if (index == currentTouchIndex) {
                    drawText(s, offsetX, baseLine.toFloat(), highLightPaint)
                } else {
                    drawText(s, offsetX, baseLine.toFloat(), paint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //计算当前触摸的字母
        event?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE
                -> {
                    currentTouchIndex = (y / itemHeight).toInt()
                }
                else -> {
                }
            }
        }
        return true
    }

}