package com.silver.fox.view

import android.util.TypedValue
import android.view.View

/**
 * @Author fox
 * @Date 2020/2/24 14:58
 */
fun View.px2sp(sp: Int) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    sp.toFloat(),
    resources.displayMetrics
).toInt()
