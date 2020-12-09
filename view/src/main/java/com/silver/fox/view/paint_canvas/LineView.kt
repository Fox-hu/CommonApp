package com.silver.fox.view.paint_canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/9 16:59
 */
class LineView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        paint.color = Color.DKGRAY
        canvas?.drawColor(Color.LTGRAY)
        //需要注意的是，只有当 Style 是 STROKE、FILL_AND_STROKE 时绘制才有效。
        //宽度为10的线
        paint.strokeWidth = 10f
        canvas?.drawLine(100f, 100f, 200f, 200f, paint)
    }
}