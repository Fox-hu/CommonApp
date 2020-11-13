package com.silver.fox.recycleview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.silver.fox.ext.logi

/**
 * @Author fox.hu
 * @Date 2020/5/20 10:11
 */
class DataBindingRefreshLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
    val isCanScroll = booleanArrayOf(true)
    var canScroll = false

    // 判定为横向滑动时，x方向的滑动最小值。
    // 理论上不要修改此值。减小该值可以提高灵敏度，但是太敏感也未必是好事
    private val HORIZONTAL_DRAG_MINI_DISTANCE = 0

    // 记录手指按下的位置
    private var startY = 0f
    private var startX = 0f
    private var onOffsetChangedListener: AppBarLayout.OnOffsetChangedListener? = null

    // 当前是否正在进行横向滑动
    private var mIsHorizontalDrag = false
    fun autoRefresh() {
        if (!isRefreshing) {
            post { isRefreshing = true }
        }
    }

    fun stopRefresh() {
        post { isRefreshing = false }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = ev.y
                startX = ev.x
                // 初始化标记
                mIsHorizontalDrag = false
            }
            MotionEvent.ACTION_MOVE -> {
                // 如果正在横向滑动，那么不拦截它的事件，直接return false；
                if (mIsHorizontalDrag) {
                    return false
                }

                // 计算滑动距离，
                val endY = ev.y
                val endX = ev.x
                val distanceX = Math.abs(endX - startX)
                val distanceY = Math.abs(endY - startY)
                // 如果X轴位移大于1.2倍Y轴位移，那么判定本次滑动为横向滑动。
                // 1.2倍这个值也可以改动，如果减小该值，横向滑动的判定条件则变得更宽松。
                // 可以近似地把2看作滑动的路径与水平方向的倾斜程度，
                if (distanceX > HORIZONTAL_DRAG_MINI_DISTANCE && distanceX > 1.2 * distanceY) {
                    mIsHorizontalDrag = true
                    return false
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->                 // 初始化标记
                mIsHorizontalDrag = false
        }

        // 除了判定为横向滑动，否则将事件交给父类进行处理。
        return super.onInterceptTouchEvent(ev)
    }

    override fun canChildScrollUp(): Boolean {
        return handleVerticalScroll(getChildAt(0))
    }

    protected fun handleVerticalScroll(target: View): Boolean {
        if (target is RecyclerView) {
            val recyclerView: RecyclerView = target
            canScroll = recyclerView.canScrollVertically(-1)
            return canScroll
        } else if (target is CoordinatorLayout) {
            //如果外部是CoordinatorLayout，只需要处理最外层的就OK了，不需要遍历到里面的列表
            val child: View = target.getChildAt(0)
            if (child is AppBarLayout) {
                if (onOffsetChangedListener == null) { //防止多次add
                    onOffsetChangedListener =
                        AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                            //展开状态
                            isCanScroll[0] = verticalOffset === 0
                        }
                    child.addOnOffsetChangedListener(onOffsetChangedListener)
                }
            }
            canScroll = !isCanScroll[0]
            return canScroll
        } else if (target is ViewGroup) {
            //todo 此处没有处理一个布局多个recyclerView的情况(外面 不是appbar)
            for (i in 0 until target.childCount) {
                val view = target.getChildAt(i)
                canScroll = handleVerticalScroll(view)
            }
        } else {
            canScroll = target.canScrollVertically(-1)
        }
        return canScroll
    }

    fun setPullDownEnable(isEnable: Boolean) {
        isEnabled = isEnable
    }
}