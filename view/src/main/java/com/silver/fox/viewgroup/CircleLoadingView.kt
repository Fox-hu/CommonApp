package com.silver.fox.viewgroup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.silver.fox.ext.logi
import com.silver.fox.viewext.dp2px

/**
 * @Author fox
 * @Date 2020/5/1 13:12
 */
class CircleLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val leftCircle: CircleView = CircleView(context, 10f).apply { color = Color.RED }
    private val centerCircle: CircleView = CircleView(context, 10f).apply { color = Color.BLUE }
    private val rightCircle: CircleView = CircleView(context, 10f).apply { color = Color.GREEN }
    private val TRANSLATION_DISTANCE = dp2px(20f).toFloat()
    private var isStop = false

    //颜色切换的顺序是左给中 中给右 右给左
    init {
        addView(leftCircle)
        addView(rightCircle)
        addView(centerCircle)
        post {
            expendAnimation()
        }
    }

    private fun expendAnimation() {
        if (isStop) {
            return
        }
        "expendAnimation".logi("CircleLoadingView")
        val transLeft =
            ObjectAnimator.ofFloat(leftCircle, "translationX", 0f, -TRANSLATION_DISTANCE)
        val transRight =
            ObjectAnimator.ofFloat(rightCircle, "translationX", 0f, TRANSLATION_DISTANCE)
        AnimatorSet().apply {
//            interpolator = DecelerateInterpolator()
            playTogether(transLeft, transRight)
            duration = 500
            doOnEnd {
                shirkAnimation()
            }
        }.start()
    }

    private fun shirkAnimation() {
        if (isStop) {
            return
        }
        "shirkAnimation".logi("CircleLoadingView")
        val transLeft =
            ObjectAnimator.ofFloat(leftCircle, "translationX", -TRANSLATION_DISTANCE, 0f)
        val transRight =
            ObjectAnimator.ofFloat(rightCircle, "translationX", TRANSLATION_DISTANCE, 0f)
        AnimatorSet().apply {

//            interpolator = AccelerateInterpolator()
            playTogether(transLeft, transRight)
            duration = 500
            doOnEnd {
                val centerColor = centerCircle.color
                val rightColor = rightCircle.color
                centerCircle.color = leftCircle.color
                rightCircle.color = centerColor
                leftCircle.color = rightColor
                expendAnimation()
            }
        }.start()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isStop = true
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            leftCircle.clearAnimation()
            rightCircle.clearAnimation()
            val parent = parent as ViewGroup?
            parent?.let {
                it.removeView(this)
                removeAllViews()
            }
            isStop = true
        }
    }
}

class CircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        operator fun invoke(context: Context, width: Float): CircleView {
            return CircleView(context).apply {
                val params =
                    RelativeLayout.LayoutParams(dp2px(width), dp2px(width))
                params.addRule(RelativeLayout.CENTER_IN_PARENT)
                layoutParams = params
            }
        }
    }

    private val paint: Paint by lazy {
        Paint().apply {
            //抗锯齿
            isAntiAlias = true
            //抗抖动
            isDither = true
        }
    }

    var color: Int = 0
        set(value) {
            field = value
            paint.color = color
            invalidate()
        }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            val cx = width / 2
            val cy = height / 2
            drawCircle(cx.toFloat(), cy.toFloat(), cx.toFloat(), paint)
        }
    }

}