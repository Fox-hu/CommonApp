package com.silver.fox.view.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * @Author fox.hu
 * @Date 2020/12/11 14:06
 */
//region可以用来绘制各种各样的形状
class RegionView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint().apply { color = Color.RED }
    val rect = Rect(100, 100, 300, 300)
    val region = Region(rect)

    val region1 = Region(400, 100, 500, 150)
    val rect1 = Rect(400, 100, 450, 300)

    val path = Path().apply {
        addOval(RectF(700f, 50f, 850f, 500f), Path.Direction.CW)
    }

    val oriRegion = Region()
    val clipRegion = Region(700, 50, 850, 200)

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawColor(Color.LTGRAY)
            //单个区域 正方形
            drawRegion(this, region, paint)
            //合并区域
            region1.union(rect1)
            drawRegion(this, region1, paint)
            //绘制裁剪区域
            oriRegion.setPath(path, clipRegion)
            drawRegion(canvas, oriRegion, paint)
        }
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