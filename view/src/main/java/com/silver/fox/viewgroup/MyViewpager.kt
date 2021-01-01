package com.silver.fox.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.ViewConfigurationCompat
import com.silver.fox.ext.logi

/**
 * @Author Fox
 * @Date 2021/1/1 22:05
 */
class MyViewpager @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    val scroller: Scroller by lazy {
        Scroller(context)
    }

    //判定为拖动的最小移动像素数
    val touchSlop: Int by lazy {
        ViewConfigurationCompat.getScaledHoverSlop(ViewConfiguration.get(context))
    }

    //手指按下时的屏幕坐标
    var xDown = 0f

    //手指移动时所处的屏幕坐标
    var xMove = 0f

    //上次触发ACTION_MOVE事件时的屏幕坐标
    var xLastMove = 0f

    var leftBorder = 0

    var rightBorder = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.layout(i * child.measuredWidth, 0, (i + 1) * child.measuredWidth, child.measuredHeight)
            }
            leftBorder = getChildAt(0).left
            rightBorder = getChildAt(childCount - 1).right
            "onLayout $leftBorder = leftBorder , $rightBorder = rightBorder".logi("MyViewpager")
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                xDown = ev.rawX
                xLastMove = xDown
                "onInterceptTouchEvent ACTION_DOWN $xDown = xDown , $xLastMove = xLastMove".logi("MyViewpager")
            }

            MotionEvent.ACTION_MOVE -> {
                xMove = ev.rawX
                val diff = Math.abs(xMove - xDown)
                xLastMove = xMove
                "onInterceptTouchEvent ACTION_MOVE $xDown = xDown , $xLastMove = xLastMove".logi("MyViewpager")
                if (diff > touchSlop) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                xMove = event.rawX
                val scrolledX = (xLastMove - xMove).toInt()
                "onTouchEvent ACTION_MOVE $scrolledX = scrolledX , $xLastMove = xLastMove, $xMove = xMove ,$scrollX = scrollX, $width =width".logi("MyViewpager")
                if (scrollX + scrolledX < leftBorder) {
                    scrollTo(leftBorder, 0)
                    return true
                } else if (scrollX + width + scrolledX > rightBorder) {
                    scrollTo(rightBorder - width, 0)
                    return true
                }
                scrollBy(scrolledX, 0)
                xLastMove = xMove
            }

            MotionEvent.ACTION_UP -> {
                val targetIndex = (scrollX + width / 2) / width
                val dx = targetIndex * width - scrollX
                scroller.startScroll(scrollX, 0, dx, 0)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            "computeScroll ${scroller.currX} = scroller.currX , ${scroller.currY} = scroller.currY".logi("MyViewpager")
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }
}