package com.silver.fox.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import androidx.core.widget.ListViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.Callback
import androidx.customview.widget.ViewDragHelper.create
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silver.fox.ext.logi

/**
 * 仿汽车之家的折叠列表
 * @Author fox
 * @Date 2020/4/3 14:26
 */

class VerticalDragListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val TAG: String = VerticalDragListView::class.java.simpleName

    private lateinit var dragListView: View
    private var menuHeight: Int = 0
    private var currentY: Float = 0f
    private var isMenuOpen: Boolean = false

    //ViewDragHelper这个类是用来帮助我们来操作viewgroup中的子类拖拽事件的
    private val dragHelper: ViewDragHelper by lazy {
        create(this, callback)
    }

    private val callback: Callback = object : Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            //该子view是否可以拖动
            return dragListView == child
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            //垂直拖动移动的位置 如果不写是不能拖动的
            var ret = top
            if (ret < 0) {
                ret = 0
            }

            if (ret >= menuHeight) {
                ret = menuHeight
            }
            return ret
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            //水平拖动移动的位置 设置left就可以水平拖动任意距离
            // return left
            return super.clampViewPositionHorizontal(child, left, dx)
        }

        //参数是速度
        //松手后的回弹
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            dragListView.top.toString().logi(TAG)
            if (releasedChild == dragListView) {
                if (dragListView.top >= menuHeight / 2) {
                    dragHelper.settleCapturedViewAt(0, menuHeight)
                    isMenuOpen = true
                } else {
                    dragHelper.settleCapturedViewAt(0, 0)
                    isMenuOpen = false
                }
                //一定要调用这个 否则不滑动
                invalidate()
            }
        }
    }

    //响应滚动事件一定要重写
    override fun computeScroll() {
        if (dragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 2) {
            throw RuntimeException("2 child view only")
        }

        dragListView = getChildAt(1)
        //这样是拿不到高度的 必须要在测量完成之后才能拿到
        //menuHeight = getChildAt(0).measuredHeight
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        ev?.apply {
            //菜单打开时全部拦截
            if (isMenuOpen) {
                return true
            }
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    currentY = y
                    //一定要让dragHelper响应一下down事件 否则再想拦截就不行了
                    dragHelper.processTouchEvent(ev)
                }
                MotionEvent.ACTION_MOVE -> {
                    //向下滑动 && 滚到到了顶部 自己处理了不让子view处理
                    if (y - currentY > 0 && !canChildScrollUp()) {
                        return true
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    //从swipeRefreshLayout中抄的 判断是否能滚动 不能滚动了则说明到了顶部
    fun canChildScrollUp(): Boolean {
        return if (dragListView is ListView) {
            ListViewCompat.canScrollList(
                (dragListView as ListView?)!!,
                -1
            )
        } else dragListView.canScrollVertically(-1)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        //一定要return true 否则不能拖动
        dragHelper.processTouchEvent(event)
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        menuHeight = getChildAt(0).measuredHeight
    }
}