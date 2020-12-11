package com.silver.fox.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/11 15:13
 */
class CanvasView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.LTGRAY)
            drawRect(canvas, Color.GREEN)
            translate(350f, 250f)
            //平移画布后，坐标系也会被平移 所以红色矩形会出现在右下角
            drawRect(canvas, Color.RED)
        }
    }

    private fun drawRect(canvas: Canvas, colorInt: Int) {
        val paint = Paint().apply { color = colorInt }
        val rect = Rect(50, 50, 200, 200)
        canvas.drawRect(rect, paint)
    }
}