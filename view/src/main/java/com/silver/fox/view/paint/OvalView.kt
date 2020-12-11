package com.silver.fox.view.paint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @Author Fox
 * @Date 2020/12/9 21:27
 */
class OvalView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val ovalPaint = Paint().apply {
        color = Color.LTGRAY
    }

    //弧线画笔
    val arcPaint1 = Paint().apply {
        style = Paint.Style.FILL
        color = Color.GREEN
    }

    val arcPaint2 = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.GREEN
    }

    val rect1 = RectF(100f, 100f, 200f, 200f)
    val path1 = Path().apply {
        //指定角度
        arcTo(rect1, 0f, 90f)
    }

    val rect2 = RectF(300f, 100f, 400f, 200f)
    val path2 = Path().apply {
        //指定角度
        arcTo(rect2, 0f, 90f)
    }
    override fun onDraw(canvas: Canvas?) {
        //填充
        canvas?.apply {
            //绘制背景
            drawOval(rect1, ovalPaint)
            //绘制弧线
            drawPath(path1, arcPaint1)
        }

        //描边
        canvas?.apply {
            drawOval(rect2, ovalPaint)
            drawPath(path2, arcPaint2)
        }
    }
}