package com.silver.fox.view.paint_canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/9 14:05
 */
class PaintColorView
@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val paint = Paint()


    override fun onDraw(canvas: Canvas?) {
        val radius = 50f
        //设置背景 设置画布背景要在其他图形绘制前设置，否则设置好的背景色会覆盖原有的图形。
        //drawColor(int color)
        //drawARGB(int a, int r, int g, int b)
        //drawRGB(int r, int g, int b)
        canvas?.drawColor(Color.DKGRAY)

        //红色圆 无抗锯齿
        paint.color = Color.RED
        paint.strokeWidth = 20f  //当画笔的 Style 是 STROKE 或 FILL_AND_STROKE 时画笔宽度才有意义。
        paint.style = Paint.Style.STROKE //描边
        canvas?.drawCircle(100f, 100f, radius, paint)

        //绿色圆
        paint.color = Color.GREEN
        paint.style = Paint.Style.FILL//填充
        paint.isAntiAlias = true //开启抗锯齿
        canvas?.drawCircle(300f, 100f, radius, paint)

        //蓝色圆
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL_AND_STROKE//填充且描边
        canvas?.drawCircle(500f, 100f, radius, paint)
    }
}
