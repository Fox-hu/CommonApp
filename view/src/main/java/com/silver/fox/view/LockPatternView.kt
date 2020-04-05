package com.silver.fox.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.silver.fox.math.checkInRound
import com.silver.fox.math.distance

/**
 * 九宫格
 * @Author fox
 * @Date 2020/4/4 13:22
 */
class LockPatternView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var isInit = false

    //外圆的半径
    private var dotRadius = 0f

    //9个点
    private var points: Array<Array<Point>> = Array(3) { Array(3) { Point(0, 0, 0) } }

    //画笔
    private lateinit var linePaint: Paint
    private lateinit var pressedPaint: Paint
    private lateinit var errorPaint: Paint
    private lateinit var normalPaint: Paint

    //颜色
    private val outerPressedColor = 0xff8cbad8.toInt()
    private val innerPressedColor = 0xff0596f6.toInt()
    private val outerNormalColor = 0xffd9d9d9.toInt()
    private val innerNormalColor = 0xff92929.toInt()
    private val outerErrorColor = 0xff901032.toInt()
    private val innerErrorColor = 0xffea0945.toInt()

    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            initDot()
            initPaint()
            isInit = true
        }

        canvas?.run {
            drawShow(canvas)
        }
    }

    private fun drawShow(canvas: Canvas) {
        points.forEach {
            it.forEach { point ->
                when (point.status) {
                    Point.STATUS.STATUS_NORMAL -> {
                        normalPaint.color = outerNormalColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius.toFloat(),
                            normalPaint
                        )
                        normalPaint.color = innerNormalColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius / 6.toFloat(),
                            normalPaint
                        )
                    }
                    Point.STATUS.STATUS_PRESSED -> {
                        pressedPaint.color = outerPressedColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius.toFloat(),
                            pressedPaint
                        )
                        pressedPaint.color = innerPressedColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius / 6.toFloat(),
                            pressedPaint
                        )
                    }
                    Point.STATUS.STATUS_ERROR -> {
                        errorPaint.color = outerErrorColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius.toFloat(),
                            errorPaint
                        )
                        errorPaint.color = outerErrorColor
                        canvas.drawCircle(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            dotRadius / 6.toFloat(),
                            errorPaint
                        )
                    }
                }
            }
        }

        //绘制两个点直接的连线和箭头
        drawLine(canvas)
    }

    private fun drawLine(canvas: Canvas) {
        if (selectPoints.size >= 1) {
            //两个点之间需要绘制一条线和箭头
            selectPoints.reduce { first, second ->
                drawLine(first, second, canvas, linePaint)
            }
            val lastPoint = selectPoints.last()
//            var lastPoint = selectPoints[0]
//
//            for (index in 1 until selectPoints.size) {
//                drawLine(lastPoint, selectPoints[index], canvas, linePaint)
//                lastPoint = selectPoints[index]
//            }

            //绘制最后一个点到手指当前位置的连线
            //如果手指在内圆里就不要绘制
            val isInnerPoint = checkInRound(
                lastPoint.centerX.toFloat(), lastPoint.centerY.toFloat(),
                currentX, currentY, dotRadius / 4
            )
            if (!isInnerPoint && isTouchPoint) {
                drawLine(
                    lastPoint,
                    Point(currentX.toInt(), currentY.toInt(), -1),
                    canvas,
                    linePaint
                )
            }
        }

    }

    private fun drawLine(start: Point, end: Point, canvas: Canvas, paint: Paint): Point {
        //等比三角形的比例关系换算
        val distance = distance(
            start.centerX.toDouble(), start.centerY.toDouble(),
            end.centerX.toDouble(), end.centerY.toDouble()
        )

        val dx = end.centerX - start.centerX
        val dy = end.centerY - start.centerY
        val rx = ((dx * (dotRadius / 6)) / distance).toFloat()
        val ry = ((dy * (dotRadius / 6)) / distance).toFloat()
        canvas.drawLine(
            start.centerX + rx, start.centerY + ry,
            end.centerX - rx, end.centerY - ry, paint
        )
        return end
    }

    private var currentX = 0f
    private var currentY = 0f
    private var isTouchPoint = false
    private var selectPoints = ArrayList<Point>()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        currentX = event.x
        currentY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //判断手指是不是按在一个宫格上面
                val point = point
                point?.run {
                    isTouchPoint = true
                    selectPoints.plusAssign(this)
                    point.status = Point.STATUS.STATUS_PRESSED
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isTouchPoint) {
                    //只有按下时在点上 才能响应move事件
                    val point = point
                    point?.let {
                        if (!selectPoints.contains(it)) {
                            selectPoints.plusAssign(it)
                        }
                        point.status = Point.STATUS.STATUS_PRESSED
                    }
                }

            }

            MotionEvent.ACTION_UP -> {
                isTouchPoint = false
            }
        }
        invalidate()
        return true
    }

    /**
     * 三个点状态的画笔 线的画笔 箭头的画笔
     * */
    private fun initPaint() {
        //线的画笔
        linePaint = Paint()
        linePaint.color = innerPressedColor
        linePaint.style = Paint.Style.STROKE
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = (dotRadius / 9).toFloat()

        //按下的画笔
        pressedPaint = Paint()
        pressedPaint.style = Paint.Style.STROKE
        pressedPaint.isAntiAlias = true
        pressedPaint.strokeWidth = (dotRadius / 6).toFloat()

        //错误的画笔
        errorPaint = Paint()
        errorPaint.style = Paint.Style.STROKE
        errorPaint.isAntiAlias = true
        errorPaint.strokeWidth = (dotRadius / 6).toFloat()

        //默认的画笔
        normalPaint = Paint()
        normalPaint.style = Paint.Style.STROKE
        normalPaint.isAntiAlias = true
        normalPaint.strokeWidth = (dotRadius / 9).toFloat()
    }

    private fun initDot() {
        //九个宫格 存到集合point[3][3]
        //有值 计算中心位置
        val width = width
        val height = height

        //单元格宽度
        val squareWidth = width / 3
        val offsetY = (height - width) / 2

        dotRadius = (width / 12).toFloat()

        points[0][0] = Point(squareWidth / 2, offsetY + squareWidth / 2, 0)
        points[0][1] = Point(squareWidth * 3 / 2, offsetY + squareWidth / 2, 1)
        points[0][2] = Point(squareWidth * 5 / 2, offsetY + squareWidth / 2, 2)
        points[1][0] = Point(squareWidth / 2, offsetY + squareWidth * 3 / 2, 3)
        points[1][1] = Point(squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4)
        points[1][2] = Point(squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5)
        points[2][0] = Point(squareWidth / 2, offsetY + squareWidth * 5 / 2, 6)
        points[2][1] = Point(squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7)
        points[2][2] = Point(squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8)
    }

    class Point(var centerX: Int, var centerY: Int, var index: Int) {
        enum class STATUS {
            STATUS_NORMAL, STATUS_PRESSED, STATUS_ERROR
        }

        var status = STATUS.STATUS_NORMAL
    }

    private val point: Point?
        get() {
            for (i in 0..2) {
                for (point in points[i]) {
                    if (checkInRound(
                            point.centerX.toFloat(),
                            point.centerY.toFloat(),
                            currentX,
                            currentY,
                            dotRadius
                        )
                    ) {
                        return point
                    }
                }
            }
            return null
        }

}