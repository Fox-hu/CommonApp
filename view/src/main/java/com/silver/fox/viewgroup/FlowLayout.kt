package com.silver.fox.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @Author fox.hu
 * @Date 2021/1/20 17:23
 */
class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0..childCount) {
            getChildAt(i)?.apply {
                measureChild(this, widthMeasureSpec, heightMeasureSpec)
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var lineWidth = 0
        var lineHeight = 0
        val w = measuredWidth

        for (i in 0..childCount) {
            getChildAt(i)?.apply {
                val width = measuredWidth
                val height = measuredHeight
                //换行
                if (width + lineWidth > w) {
                    top += lineHeight
                    left = l
                    lineHeight = height
                    lineWidth = width
                    layout(left, top, left + width, top + height)
                } else {
                    left = lineWidth
                    layout(left, top, left + width, top + height)
                    lineHeight = Math.max(lineHeight, height)
                    lineWidth += width

                }
            }
        }
    }
}