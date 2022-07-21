package com.silver.fox.ext

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.silver.fox.Ktx

@ColorInt
fun Int.getColor(context: Context = Ktx.app): Int = ContextCompat.getColor(context, this)

fun Int.getString(context: Context = Ktx.app): String = context.getString(this)

fun Int.dp2px(context: Context? =  Ktx.app): Int {
    if (this <= 0) return 0
    val scale = context?.resources?.displayMetrics?.density ?: 0f
    return (this * scale + 0.5f).toInt()
}

fun Float.dp2px(context: Context? =  Ktx.app): Int {
    if (this <= 0) return 0
    val scale = context?.resources?.displayMetrics?.density ?: 0f
    return (this * scale + 0.5f).toInt()
}

fun Int.getStringArray(context: Context = Ktx.app): Array<String> =
    context.resources.getStringArray(this)

fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Int): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )

val Context.screenRadio: Float
    get() {
        return if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenHeight.toFloat() / screenWidth.toFloat()
        } else {
            screenWidth.toFloat() / screenHeight.toFloat()
        }
    }
