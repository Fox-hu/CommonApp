package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.silver.fox.viewext.config

/**
 * 颜色可以变色的TextView
 * @Author fox
 * @Date 2020/2/25 18:26
 */
class ColorTrackTextView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var originColor = Color.RED
    private var changeColor = Color.BLUE

    private var originPaint = Paint()
    private var changePaint = Paint()

    var currentProgress = 0.0f
        @Synchronized set(value) {
            field = value
            invalidate()
        }

    var direction = Direction.LEFT_TO_RIGHT

    enum class Direction {
        LEFT_TO_RIGHT, RIGHT_TO_LEFT
    }

    init {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.view_ColorTrackTextView)
        array.apply {
            originColor =
                getColor(R.styleable.view_ColorTrackTextView_view_originColor, originColor)
            changeColor =
                getColor(R.styleable.view_ColorTrackTextView_view_changeColor, changeColor)
            recycle()
        }

        originPaint.config(originColor, textSize)
        changePaint.config(changeColor, textSize)
    }

    //一个文字两种颜色
    //利用clipRect方法
    override fun onDraw(canvas: Canvas?) {
        val middle = (currentProgress * width).toInt()
        if (direction == Direction.LEFT_TO_RIGHT) {
            drawText(canvas, changePaint, 0, middle)
            drawText(canvas, originPaint, middle, width)
        } else {
            drawText(canvas, changePaint, width - middle, width)
            drawText(canvas, originPaint, 0, width - middle)
        }
    }

    private fun drawText(canvas: Canvas?, paint: Paint, start: Int, end: Int) {
        canvas?.save()

        //根据进度算出 裁剪区域的大小
        val progress = currentProgress * width
        canvas?.clipRect(start, 0, end, height)

        val text = text.toString()
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)

        //获取字体宽度
        val x = width / 2 - rect.width() / 2
        //获取baseline
        val fontMetricsInt = paint.fontMetricsInt
        val dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom
        val baseLine = height / 2 + dy
        //画出文字
        canvas?.drawText(text, x.toFloat(), baseLine.toFloat(), paint)

        canvas?.restore()
    }

}