package com.silver.fox.viewext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PointF
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import com.silver.fox.Ktx

/**
 * @Author fox
 * @Date 2020/2/24 14:58
 */
fun View.px2sp(sp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    resources.displayMetrics
).toInt()

fun dp2px(dpValue: Float, context: Context = Ktx.app): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5).toInt()
}

fun getScreenWidth(context: Context = Ktx.app): Int = getDisplayMetrics(
    context
).widthPixels

fun getScreenHeight(context: Context = Ktx.app): Int = getDisplayMetrics(
    context
).heightPixels

private fun getDisplayMetrics(context: Context = Ktx.app): DisplayMetrics {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    wm.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}


fun Paint.config(paintColor: Int, configTextSize: Float) {
    apply {
        isAntiAlias = true
        color = paintColor
        //防抖动
        isDither = true
        textSize = configTextSize
    }
}

//获取状态栏高度
fun Context.getStatusBarHeight(): Int {
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        return resources.getDimensionPixelOffset(resId)
    }
    return dp2px(25f, this)
}

var View.bitmap: Bitmap
    get() {
        buildDrawingCache()
        return getDrawingCache()
    }
    private set(value) {}

fun PointF.getPointByPercent(target: PointF, percent: Float): PointF {
    fun evaluateValue(fraction: Float, start: Number, end: Number): Float {
        return start.toFloat() + (end.toFloat() - start.toFloat()) * fraction
    }
    return PointF(evaluateValue(percent, x, target.x), evaluateValue(percent, y, target.y))
}

fun PointF.isAvailable(): Boolean {
    return !(x == 0f && y == 0f)
}

