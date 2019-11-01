package com.component.kotlintest.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.Scroller

class CuView : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var mStartX: Float = 0F
    var mStartY: Float = 0F

    var isFirstFinish: Boolean = false


    private val scroller = Scroller(context)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            var action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    if (!isFirstFinish) {
                        mStartX = event.rawX
                        mStartY = event.rawY
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    scrollTo((mStartX - event.rawX) as Int, (mStartY - event.rawY) as Int)
                }

                MotionEvent.ACTION_UP -> {
                    isFirstFinish = true
                }
            }
        }
        return true
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
        super.computeScroll()
    }
}