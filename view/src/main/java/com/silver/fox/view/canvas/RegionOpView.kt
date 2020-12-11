package com.silver.fox.view.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/11 14:06
 */
class RegionOpView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.DKGRAY)
            //补集
            drawRegion(Region.Op.DIFFERENCE,this,100,100)
            //反转补集
            drawRegion(Region.Op.REVERSE_DIFFERENCE,this,300,100)
            //交集
            drawRegion(Region.Op.INTERSECT,this,500,100)
            //并集
            drawRegion(Region.Op.UNION,this,100,350)
            //异或集
            drawRegion(Region.Op.XOR,this,300,350)
            //替换原有区域
            drawRegion(Region.Op.REPLACE,this,500,350)
        }
    }

    //根据区域来画矩形
    fun drawRegion(op: Region.Op, canvas: Canvas, left: Int, top: Int) {

        val paint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }

        val yellowRect = Rect(left, top, left + 50, top + 150)
        paint.color = Color.YELLOW
        canvas.drawRect(yellowRect, paint)

        val greenRect = Rect(left - 50, top + 50, left + 100, top + 100)
        paint.color = Color.GREEN
        canvas.drawRect(greenRect, paint)

        val yellowRegion = Region(yellowRect)
        val greenRegion = Region(greenRect)
        yellowRegion.op(greenRegion,op)

        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        drawRegion(canvas,yellowRegion,paint)
    }

    //根据区域来画矩形
    fun drawRegion(canvas: Canvas, region: Region, paint: Paint) {
        val regionIterator = RegionIterator(region)
        val r = Rect()
        while (regionIterator.next(r)) {
            canvas.drawRect(r, paint)
        }
    }
}