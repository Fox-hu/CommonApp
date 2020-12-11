package com.silver.fox.view.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/9 17:23
 */
class RectView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint()
    val rectF = RectF(500f, 100f, 600f, 200f)

    override fun onDraw(canvas: Canvas?) {
        paint.color = Color.DKGRAY

        //填充矩形
        paint.style = Paint.Style.FILL
        canvas?.drawRect(100f, 100f, 200f, 200f, paint)

        //描边矩形
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        canvas?.drawRect(300f, 100f, 400f, 200f, paint)

        //圆角矩形
        val xRadius = 5f
        paint.strokeWidth = 10f
        val yRadius = 10f
        canvas?.drawRoundRect(rectF, xRadius, yRadius, paint)
    }
}