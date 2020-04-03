package com.silver.fox.viewext

import android.content.Context
import android.graphics.Paint
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager

/**
 * @Author fox
 * @Date 2020/2/24 14:58
 */
fun View.px2sp(sp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    resources.displayMetrics
).toInt()

fun dp2px(context: Context, dpValue: Float): Int{
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5).toInt()
}

fun getScreenWidth(context: Context): Int = getDisplayMetrics(
    context
).widthPixels

fun getScreenHeight(context: Context): Int = getDisplayMetrics(
    context
).heightPixels

private fun getDisplayMetrics(context: Context): DisplayMetrics {
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
