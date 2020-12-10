package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.silver.fox.ext.logi

/**
 * @Author fox.hu
 * @Date 2020/12/10 14:35
 */
class SpiderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val radarPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.GREEN
    }

    val valuePaint = Paint().apply {
        color = Color.RED
    }

    var radius = 0f //网格最大半径
    var centerX = 0f //中心点x
    var centerY = 0f //中心点y
    var count = 6 //多边形边数
    val angle = (2 * Math.PI / count) //多边形的每个平分的角度

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = Math.min(h, w) / 2 * 0.9f
        centerX = w / 2f
        centerY = h / 2f
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawPolygon(canvas)  //绘制蜘蛛网格
            drawLines(canvas) //绘制网格中线
            drawRegion(canvas) //绘制数据图
        }
    }

    private fun drawPolygon(canvas: Canvas) {
        val path = Path()
        val gap = radius / (count - 1)

        for (i in 0 until count) {
            var curR = gap * i

            //重置路径
            path.reset()
            for (j in 0 until count) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY)
                } else {
                    val x = (centerX + curR * Math.cos(angle * j)).toFloat()
                    val y = (centerY + curR * Math.sin(angle * j)).toFloat()
                    path.lineTo(x, y)
                }
            }
            path.close() //闭环
            canvas.drawPath(path, radarPaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        val path = Path()
        for (i in 0 until count) {
            path.reset()
            path.moveTo(centerX, centerY)
            val x = (centerX + radius * Math.cos(angle * i)).toFloat()
            val y = (centerY + radius * Math.sin(angle * i)).toFloat()
            path.lineTo(x, y)
            canvas.drawPath(path, radarPaint)
        }
    }

    val data = floatArrayOf(2f, 5f, 1f, 6f, 4f, 5f)
    val maxValue = 6f

    private fun drawRegion(canvas: Canvas) {
        val path = Path()

        for (i in 0 until count) {
            val percent = data[i] / maxValue
            val x = (centerX + radius * Math.cos(angle * i) * percent).toFloat()
            val y = (centerY + radius * Math.sin(angle * i) * percent).toFloat()

            "percent = $percent x = $x y = $y".logi("SpiderView")

            if (i == 0) {
                path.moveTo(x, centerY)
            } else {
                path.lineTo(x, y)
            }

            //绘制小点
            valuePaint.alpha = 255
            canvas.drawCircle(x, y, 10f, valuePaint)
        }

        //绘制填充区域
        valuePaint.alpha = 127
        valuePaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(path, valuePaint)
    }
}