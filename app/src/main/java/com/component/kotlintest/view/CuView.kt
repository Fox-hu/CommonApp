package com.component.kotlintest.view

import android.content.Context
import android.util.AttributeSet
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

    init {
        val scroller = Scroller(context)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        var action = event?.action
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//
//            }
//            else -> {
//            }
//        }
//    }

}