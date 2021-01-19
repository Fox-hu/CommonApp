package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

import android.util.AttributeSet
import android.view.View

/**
 * 形状变化的view
 * @Author fox
 * @Date 2020/2/29 15:29
 */
class ShapeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint()
    private var currentShape = Shape.Circle

    private var path: Path = Path()

    enum class Shape {
        Circle, Square, Triangle
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val min = width.coerceAtMost(height)
        setMeasuredDimension(min, min)
    }

    override fun onDraw(canvas: Canvas?) {
        when (currentShape) {
            Shape.Circle -> {
                paint.color = Color.YELLOW
                val center = (width / 2).toFloat()
                canvas?.drawCircle(center, center, center, paint)
            }
            Shape.Square -> {
                paint.color = Color.BLUE
                canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }
            Shape.Triangle -> {
                //使用path来绘制一个三角形
                paint.color = Color.RED
                path.moveTo((width / 2).toFloat(), 0f)
                path.lineTo(0f, height.toFloat())
                path.lineTo(width.toFloat(), height.toFloat())
                path.close()
                canvas?.drawPath(path,paint)
            }
        }
    }

    fun exchange() {
        currentShape = when (currentShape) {
            Shape.Circle -> Shape.Square
            Shape.Square -> Shape.Triangle
            Shape.Triangle -> Shape.Circle
        }
        invalidate()
    }
}