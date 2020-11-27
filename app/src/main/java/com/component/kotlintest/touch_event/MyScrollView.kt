package com.component.kotlintest.touch_event

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ScrollView

/**
 * @Author fox.hu
 * @Date 2020/11/27 16:06
 */
class MyScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {

    private var mLastY = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercept = super.onInterceptTouchEvent(ev)
        ev?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN ->
                    intercept = false

                MotionEvent.ACTION_MOVE -> {
                    val listView = (getChildAt(0) as ViewGroup).getChildAt(1) as ListView
                    //listView顶部可见 并且是向下滑动 则scrollView需要接管滑动事件
                    //listView底部可见 并且是向上滑动 则scrollView需要接管滑动事件
                    //否则事件交给listView
                    intercept = listView.firstVisiblePosition == 0 && ev.y - mLastY > 0
                            || listView.lastVisiblePosition == listView.count - 1 && ev.y - mLastY < 0
                }
                MotionEvent.ACTION_CANCEL -> intercept = false
            }
            mLastY = y
        }
        Log.i("MyScrollView", "intercept=$intercept")
        return intercept
    }
}