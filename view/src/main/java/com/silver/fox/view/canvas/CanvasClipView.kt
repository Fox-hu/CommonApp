package com.silver.fox.view.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/11 15:28
 */

class CanvasClipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val rect = Rect(100, 100, 400, 400)

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.RED)
            canvas.clipRect(rect)
            canvas.drawColor(Color.GREEN)
        }
    }
}