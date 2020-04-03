package com.silver.fox.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 仿android的ratingbar
 * @Author fox
 * @Date 2020/3/2 12:49
 */
class RatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var gradeNum = 5
    private var currentGrade = 0
    private var starNormalBM: Bitmap
    private var starFocusBM: Bitmap

    init {
        val array =
            context.obtainStyledAttributes(attrs, R.styleable.view_RatingBar)
        array.apply {
            gradeNum = getInt(R.styleable.view_RatingBar_view_gradeNum, gradeNum)
            val starNormalId = getResourceId(R.styleable.view_RatingBar_view_starNormal, 0)
            val starFocusId = getResourceId(R.styleable.view_RatingBar_view_starFocus, 0)
            if (starFocusId == 0 || starNormalId == 0) {
                throw RuntimeException("请设置属性")
            }
            starNormalBM = BitmapFactory.decodeResource(resources, starNormalId)
            starFocusBM = BitmapFactory.decodeResource(resources, starFocusId)

            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = starNormalBM.height
        val width = starNormalBM.width * gradeNum //需要加上星星间隔 和padding
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            for (i in 0..gradeNum) {
                //每画一个星星 就向右偏移一个星星的宽度
                val offset = (i * starNormalBM.width).toFloat()
                if (currentGrade > i) {
                    drawBitmap(starFocusBM, offset, 0f, null)
                } else {
                    drawBitmap(starNormalBM, offset, 0f, null)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.apply {
            //移动、抬起、按下的处理逻辑都一样，判断手指位置，根据当前位置计算出分数
            //up事件不处理 减少ondraw调用
            when (action) {
                MotionEvent.ACTION_DOWN,
                MotionEvent.ACTION_MOVE -> {
                    val currentX = x
                    var currentNum = (currentX / starNormalBM.width + 1).toInt()
                    //1 处理范围问题
                    currentNum = Math.max(0, currentNum)
                    currentNum = Math.min(currentNum, gradeNum)
                    //2 分数相同不绘制 减少ondraw调用
                    if (currentNum != currentGrade) {
                        currentGrade = currentNum
                        invalidate()
                    }
                }
                else -> {
                }
            }
        }
        return true
    }

}