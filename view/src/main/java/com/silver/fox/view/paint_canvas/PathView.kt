package com.silver.fox.view.paint_canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @Author Fox
 * @Date 2020/12/9 21:01
 */
class PathView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val paint1 by lazy {
        Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.FILL
        }
    }
    val paint2 by lazy {
        Paint().apply {
            color = Color.RED
            strokeWidth = 10f
            style = Paint.Style.STROKE
        }
    }

    val path1 = Path().apply {
        //第一条线的起点
        moveTo(100f, 100f)
        //第一条线的终点 第二条线的起点
        lineTo(400f, 200f)
        //第二条线的终点
        lineTo(100f, 200f)
        //绘制第三条线 形成闭环
        close()
    }

    val path2 = Path().apply {
        //第一条线的起点
        moveTo(200f, 100f)
        //第一条线的终点 第二条线的起点
        lineTo(500f, 200f)
        //第二条线的终点
        lineTo(500f, 100f)
        //绘制第三条线 形成闭环
        close()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawColor(Color.DKGRAY)
        //不填充path区域
        canvas?.drawPath(path1, paint1)
        //填充path区域
        canvas?.drawPath(path2, paint2)
    }
}