package com.silver.fox.viewgroup

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import com.silver.fox.math.random
import com.silver.fox.view.R

/**
 * 花束点赞效果
 * @Author fox
 * @Date 2020/5/4 12:33
 */
class LoveLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var imgWidth = 0
    private var imgHeight = 0

    private val imgRes = intArrayOf(R.drawable.pl_blue, R.drawable.pl_red, R.drawable.pl_yellow)
    private val interceptors = listOf<Interpolator>(
        AccelerateInterpolator(),
        AccelerateInterpolator(),
        DecelerateInterpolator(),
        LinearInterpolator()
    )

    init {
        val drawable = ContextCompat.getDrawable(context, R.drawable.pl_blue)
        imgWidth = drawable?.intrinsicWidth ?: 0
        imgHeight = drawable?.intrinsicHeight ?: 0
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    fun addLove() {
        val iv = ImageView(context).apply {
            setImageResource(imgRes.random())
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                addRule(ALIGN_PARENT_BOTTOM)
                addRule(CENTER_HORIZONTAL)
            }
            layoutParams = params
        }
        addView(iv)

        val alpha = ObjectAnimator.ofFloat(iv, "alpha", 0.3f, 1f)
        val scaleX = ObjectAnimator.ofFloat(iv, "scaleX", 0.3f, 1f)
        val scaleY = ObjectAnimator.ofFloat(iv, "scaleY", 0.3f, 1f)

        val innerAni = AnimatorSet().apply {
            duration = 350
            playTogether(alpha, scaleX, scaleY)
        }

        AnimatorSet().apply {
            playSequentially(innerAni, getBezierAnimator(iv))
            doOnEnd {
                removeView(iv)
            }
        }.start()
    }

    private fun getBezierAnimator(iv: ImageView): Animator {
        //确定四个点 并采用贝塞尔公式 进行曲线规划 其中p2一定要高于p1
        //p0是底部爱心图片左上角的位置 不是中心的位置
        val point0 = PointF(((mWidth - imgWidth) / 2).toFloat(), (mHeight - imgHeight).toFloat())
        val point1 = PointF(mWidth.random.toFloat(), (mHeight / 2).random.toFloat())
        val point2 = PointF(mWidth.random.toFloat(), mHeight / 2 + (mHeight / 2).random.toFloat())
        val point3 = PointF(mWidth.random.toFloat(), 0f)

        return ObjectAnimator.ofObject(LoveTypeEvaluator(point1, point2), point0, point3).apply {
            duration = 3000
            interpolator = interceptors.random()
            addUpdateListener {
                val pointF = it.animatedValue as PointF
                iv.x = pointF.x
                iv.y = pointF.y
                //animatedFraction 是指当前动画的进行率
                alpha = 1 - animatedFraction + 0.2f
            }
        }
    }
}

class LoveTypeEvaluator(val point1: PointF, val point2: PointF) : TypeEvaluator<PointF> {

    override fun evaluate(t: Float, startValue: PointF, endValue: PointF): PointF {
        // t 是 [0,1]  开始套公式 公式有四个点 还有两个点从哪里来（构造函数中来）
        return PointF().apply {
            x =
                startValue.x * (1 - t) * (1 - t) * (1 - t) + 3 * point1.x * t * (1 - t) * (1 - t) + 3 * point2.x * t * t * (1 - t) + endValue.x * t * t * t
            y =
                startValue.y * (1 - t) * (1 - t) * (1 - t) + 3 * point1.y * t * (1 - t) * (1 - t) + 3 * point2.y * t * t * (1 - t) + endValue.y * t * t * t
        }
    }
}