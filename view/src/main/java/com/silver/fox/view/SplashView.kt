package com.silver.fox.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.core.animation.doOnEnd
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @Author fox
 * @Date 2020/5/5 14:24
 */
class SplashView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    interface LoadingState {
        fun onDraw(canvas: Canvas)
    }

    var finishListener :(Unit)-> Unit = {

    }

    private var state: LoadingState? = null

    private var currentRotationAngle: Float = 0f

    private var centerX = 0
    private var centerY = 0

    private val paint: Paint by lazy {
        Paint().apply {
            //抗锯齿
            isAntiAlias = true
            //抗抖动
            isDither = true
        }
    }

    //大圆的半径
    private var rotationRadius: Float = 0f

    //围绕的小圆半径
    private var pointRadius: Float = 0f

    //以屏幕对角线作为半径
    private var diagonalDist: Float = 0f


    private val circleColors: IntArray by lazy {
        context.resources.getIntArray(R.array.view_splash_circle_colors)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        centerX = measuredWidth / 2
        centerY = measuredHeight / 2
        rotationRadius = (measuredWidth / 4).toFloat()
        pointRadius = rotationRadius / 8
        diagonalDist = sqrt((centerX * centerX + centerY * centerY).toDouble()).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            if (state == null) {
                state = RotationState()
            }
            state!!.onDraw(canvas)
        }
    }

    fun disappear() {
        if (state is RotationState) {
            (state as RotationState).animator?.cancel()
        }
        state = MergeState()
    }

    inner class RotationState : LoadingState {
        var animator: ValueAnimator? = null


        init {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(0f, (Math.PI * 2).toFloat()).apply {
                    duration = 3000
                    addUpdateListener {
                        currentRotationAngle = it.animatedValue as Float
                        invalidate()
                    }
                    repeatCount = -1
                }
                animator?.start()
            }
        }

        override fun onDraw(canvas: Canvas) {
            val percentAngle = Math.PI * 2 / circleColors.size
            circleColors.forEachIndexed { index, i ->
                paint.color = i
                val currentAngle = index * percentAngle + currentRotationAngle
                val cx = centerX + rotationRadius * cos(currentAngle)
                val cy = centerY + rotationRadius * sin(currentAngle)
                canvas.drawCircle(cx.toFloat(), cy.toFloat(), pointRadius, paint)
            }
        }
    }

    inner class MergeState : LoadingState {
        var animator: ValueAnimator? = null
        private var currentRadius: Float = 0f

        init {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(rotationRadius, 0f).apply {
                    duration = 1500
                    addUpdateListener {
                        currentRadius = it.animatedValue as Float
                        invalidate()
                    }
                    doOnEnd {
                        state = ExpandState()
                    }
                    interpolator = AnticipateInterpolator(3f)
                }
                animator?.start()
            }
        }

        override fun onDraw(canvas: Canvas) {
            val percentAngle = Math.PI * 2 / circleColors.size
            circleColors.forEachIndexed { index, i ->
                paint.color = i
                val currentAngle = index * percentAngle + currentRotationAngle
                val cx = centerX + currentRadius * cos(currentAngle)
                val cy = centerY + currentRadius * sin(currentAngle)
                canvas.drawCircle(cx.toFloat(), cy.toFloat(), pointRadius, paint)
            }
        }
    }

    inner class ExpandState : LoadingState {
        var animator: ValueAnimator? = null
        private var holeRadius: Float = 0f

        init {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(0f, diagonalDist).apply {
                    duration = 1500
                    addUpdateListener {
                        holeRadius = it.animatedValue as Float
                        invalidate()
                    }
                    doOnEnd {
                        finishListener(Unit)
                    }
                }
                animator?.start()
            }
        }

        override fun onDraw(canvas: Canvas) {
            //画一个空心圆 strokeWidth越来越小 最后呈现一个空心的效果
            val sw = diagonalDist - holeRadius
            paint.apply {
                strokeWidth = sw
                style = Paint.Style.STROKE
                color = Color.WHITE
            }
            //这个radius是很让人费解的
            val radius = sw / 2 + holeRadius
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius, paint)
        }
    }
}