package com.silver.fox.behavior

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat

/**
 * Created by fox on 2018/1/28.
 */
class BottomNavigationBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<View>(context, attrs) {

    private var outAnimator: ObjectAnimator? = null
    private var inAnimator: ObjectAnimator? = null

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (dy > 0) {
            //上滑隐藏子控件
            if (outAnimator == null) {
                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0f, child.height.toFloat())
                outAnimator!!.duration = 200
            }
            outAnimator?.apply {
                if (!isRunning && child.translationY <= 0) {
                    start()
                }
            }

        } else if (dy < 0) {
            //下滑显示隐藏子控件
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.height.toFloat(), 0f)
                inAnimator!!.duration = 200
            }
            inAnimator?.apply {
                if (!isRunning && child.translationY >= child.height) {
                    start()
                }
            }
        }
    }
}