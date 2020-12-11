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

class CanvasRestoreIndexView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val rectGreen = Rect(100,100,500,500)
    val rectBlue = Rect(200,200,400,400)
    val rectBlack = Rect(300,300,450,450)
    val rectWhile = Rect(350,350,400,400)

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.RED)
            val state1 = save()

            clipRect(rectGreen)
            drawColor(Color.GREEN)
            val state2 = save()

            clipRect(rectBlue)
            drawColor(Color.BLUE)
            val state3 = save()

            clipRect(rectBlack)
            drawColor(Color.BLACK)
            val state4 = save()

            clipRect(rectWhile)
            drawColor(Color.WHITE)
            val state5 = save()

            //三次出栈 回到涂蓝色的的时刻
            restoreToCount(state3)
            drawColor(Color.YELLOW)
        }
    }
}