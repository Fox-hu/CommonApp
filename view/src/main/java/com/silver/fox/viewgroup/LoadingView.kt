package com.silver.fox.viewgroup

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.silver.fox.ext.logi
import com.silver.fox.view.R
import com.silver.fox.viewext.dp2px
import kotlinx.android.synthetic.main.view_loading.view.*
import kotlinx.android.synthetic.main.view_loading.view.shape_view

/**
 * 58加载数据动画
 * @Author fox
 * @Date 2020/4/6 14:56
 */
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val ANIMATOR_DURATION: Long = 300
    private var isStop = false

    init {
//        val view = inflate(context, R.layout.view_loading, null)
//        addView(view)
        //等价于
        inflate(context, R.layout.view_loading, this)
        //在post中 就是rusume中开始进行动画 如果不在post中就是oncreate
        post {
            startFallAnimator()
        }
    }

    private fun startFallAnimator() {
        if (isStop) {
            return
        }
        "startFallAnimator".logi("LoadingView")
        val shapeFallAnimator =
            ObjectAnimator.ofFloat(shape_view, "translationY", 0f, dp2px(context, 80f).toFloat())
        shapeFallAnimator.duration = ANIMATOR_DURATION
        shapeFallAnimator.interpolator = AccelerateInterpolator()

        val shapeRotationAnimator = ObjectAnimator.ofFloat(shape_view, "rotation", 0f, 60f)
        shapeRotationAnimator.duration = ANIMATOR_DURATION

        val shadowAnimator = ObjectAnimator.ofFloat(shadow_view, "scaleX", 1f, 0.3f)
        shadowAnimator.duration = ANIMATOR_DURATION
        shadowAnimator.interpolator = AccelerateInterpolator()

        val aniSet = AnimatorSet()
        aniSet.playTogether(shapeFallAnimator, shapeRotationAnimator, shadowAnimator)
        aniSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                //下落后上抛
                shape_view.exchange()
                //改变形状
                startUpAnimator()
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        aniSet.start()
    }

    private fun startUpAnimator() {
        if (isStop) {
            return
        }
        "startUpAnimator".logi("LoadingView")
        val shapeUpAnimator =
            ObjectAnimator.ofFloat(shape_view, "translationY", dp2px(context, 80f).toFloat(), 0f)
        shapeUpAnimator.duration = ANIMATOR_DURATION
        shapeUpAnimator.interpolator = DecelerateInterpolator()


        val shadowAnimator = ObjectAnimator.ofFloat(shadow_view, "scaleX", 0.3f, 1f)
        shadowAnimator.duration = ANIMATOR_DURATION
        shadowAnimator.interpolator = DecelerateInterpolator()

        val aniSet = AnimatorSet()
        aniSet.playTogether(shapeUpAnimator, shadowAnimator)
        aniSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                startFallAnimator()
            }
        })
        aniSet.start()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            shape_view.clearAnimation()
            shadow_view.clearAnimation()
            val parent = parent as ViewGroup?
            parent?.let {
                it.removeView(this)
                removeAllViews()
            }
            isStop = true
        }
    }


    //使用关键帧方式
    private fun startAnimator() {
        //下落 上浮动画
        val startFrame = Keyframe.ofFloat(0f, 0f)
        val midFrame = Keyframe.ofFloat(0.5f, dp2px(context, 80f).toFloat())
        val endFrame = Keyframe.ofFloat(1f, 0f)
        val holder =
            PropertyValuesHolder.ofKeyframe("translationY", startFrame, midFrame, endFrame)

        val shapeAnimator = ObjectAnimator.ofPropertyValuesHolder(shape_view, holder)
        shapeAnimator.duration = ANIMATOR_DURATION
        shapeAnimator.repeatCount = ValueAnimator.INFINITE

        //阴影缩放动画
        val startFrame1 = Keyframe.ofFloat(0f, 1f)
        val midFrame1 = Keyframe.ofFloat(0.5f, 0.3f)
        val endFrame1 = Keyframe.ofFloat(1f, 1f)
        val holder1 =
            PropertyValuesHolder.ofKeyframe("scaleX", startFrame1, midFrame1, endFrame1)

        val shadowAnimator = ObjectAnimator.ofPropertyValuesHolder(shadow_view, holder1)
        shadowAnimator.duration = ANIMATOR_DURATION
        shadowAnimator.repeatCount = ValueAnimator.INFINITE

        val set = AnimatorSet()
        set.playTogether(shapeAnimator, shadowAnimator)
        set.start()
    }
}