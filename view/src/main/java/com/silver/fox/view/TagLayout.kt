package com.silver.fox.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup


/**
 * 自动换行的layout
 * @Author fox
 * @Date 2020/3/6 12:00
 */
class TagLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    //一行的内容使用一个List<View>来表示
    var childViews: MutableList<MutableList<View>> = mutableListOf()

    //需要注意margin

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = paddingBottom + paddingTop
        var lineWidth = paddingLeft
        childViews.clear()

        var lineChild = mutableListOf<View>()
        childViews.add(lineChild)

        var maxHeight = 0
        for (i in 0 until childCount) {
            var childView = getChildAt(i)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)

            var layoutParams = childView.layoutParams as MarginLayoutParams

            if (lineWidth + childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin > width) {
                //换行 将宽度清空，新增一个新行view到容器中
                height += childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
                lineWidth =
                    childView.measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin
                lineChild = mutableListOf()
                childViews.add(lineChild)
            } else {
                lineWidth += childView.measuredWidth + layoutParams.rightMargin + layoutParams.leftMargin
                maxHeight = Math.max(
                    childView.measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin,
                    maxHeight
                )
            }
            lineChild.add(childView)
        }

        height += maxHeight

        setMeasuredDimension(width, height)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context,attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = paddingTop
        var right = 0
        var bottom = 0

        for (childView in childViews) {
            left = paddingLeft

            //layout一行的内容
            for (view in childView) {
                val params = view.layoutParams as MarginLayoutParams
                left += params.leftMargin
                var childTop = top + params.topMargin
                right = left + view.measuredWidth
                bottom = childTop + view.measuredHeight

                view.layout(left, childTop, right, bottom)
                left += view.measuredWidth + params.rightMargin
            }

            //换行 top+ 一行的height
            var layoutParams = childView[0].layoutParams as MarginLayoutParams
            top += childView[0].measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin
        }
    }
}