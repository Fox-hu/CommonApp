package com.silver.fox.ext

import android.R
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.Serializable

inline fun <reified T : Activity> Activity.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) {

    val list = ArrayList<Pair<String, Any>?>()
    value?.let { list.add(it) }
    values?.let { list.addAll(it) }
    startActivity(getIntent<T>(flags, extra, list))
}

inline fun <reified T : Activity> Fragment.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) =
    activity?.let {
        val list = ArrayList<Pair<String, Any>?>()
        value?.let { v -> list.add(v) }
        values?.let { v -> list.addAll(v) }
        startActivity(it.getIntent<T>(flags, extra, list))
    }

inline fun <reified T : Activity> Context.startKtxActivity(
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) {
    val list = ArrayList<Pair<String, Any>?>()
    value?.let { v -> list.add(v) }
    values?.let { v -> list.addAll(v) }
    startActivity(getIntent<T>(flags, extra, list))
}

inline fun <reified T : Activity> Activity.startKtxActivityForResult(
    requestCode: Int,
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) {
    val list = ArrayList<Pair<String, Any>?>()
    value?.let { list.add(it) }
    values?.let { list.addAll(it) }
    startActivityForResult(getIntent<T>(flags, extra, list), requestCode)
}

inline fun <reified T : Activity> Fragment.startKtxActivityForResult(
    requestCode: Int,
    flags: Int? = null,
    extra: Bundle? = null,
    value: Pair<String, Any>? = null,
    values: Collection<Pair<String, Any>?>? = null
) =
    activity?.let {
        val list = ArrayList<Pair<String, Any>?>()
        value?.let { list.add(it) }
        values?.let { list.addAll(it) }
        startActivityForResult(activity?.getIntent<T>(flags, extra, list), requestCode)
    }

inline fun <reified T : Context> Context.getIntent(
    flags: Int? = null,
    extra: Bundle? = null,
    pairs: List<Pair<String, Any>?>? = null
): Intent =
    Intent(this, T::class.java).apply {
        flags?.let { setFlags(flags) }
        extra?.let { putExtras(extra) }
        pairs?.let {
            for (pair in pairs)
                pair?.let {
                    val name = pair.first
                    when (val value = pair.second) {
                        is Int -> putExtra(name, value)
                        is Byte -> putExtra(name, value)
                        is Char -> putExtra(name, value)
                        is Short -> putExtra(name, value)
                        is Boolean -> putExtra(name, value)
                        is Long -> putExtra(name, value)
                        is Float -> putExtra(name, value)
                        is Double -> putExtra(name, value)
                        is String -> putExtra(name, value)
                        is CharSequence -> putExtra(name, value)
                        is Parcelable -> putExtra(name, value)
                        is Array<*> -> putExtra(name, value)
                        is ArrayList<*> -> putExtra(name, value)
                        is Serializable -> putExtra(name, value)
                        is BooleanArray -> putExtra(name, value)
                        is ByteArray -> putExtra(name, value)
                        is ShortArray -> putExtra(name, value)
                        is CharArray -> putExtra(name, value)
                        is IntArray -> putExtra(name, value)
                        is LongArray -> putExtra(name, value)
                        is FloatArray -> putExtra(name, value)
                        is DoubleArray -> putExtra(name, value)
                        is Bundle -> putExtra(name, value)
                        is Intent -> putExtra(name, value)
                        else -> {
                        }
                    }
                }
        }
    }

fun Activity.hideKeyboard() {
    inputMethodManager?.hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    currentFocus?.clearFocus()
}

fun Activity.showKeyboard(et: EditText) {
    et.requestFocus()
    inputMethodManager?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideKeyboard(view: View) {
    inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.fullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && LiveDisplayCutout.hasDisplayCutoutAllSituations(
            this.window
        )
    ) {
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
    }
}

fun Activity.quitFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && LiveDisplayCutout.hasDisplayCutoutAllSituations(
            this.window
        )
    ) {
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
        window.attributes = lp
    }
}

fun Activity.setStatusBarColor(colorId:Int){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,colorId)
    }
}

//private val FAKE_TRANSLUCENT_VIEW_ID: Int = R.id.statusbarutil_translucent_view
//
//fun Activity.setStatusBarColor(colorId: Int, @IntRange(from = 0, to = 255) statusBarAlpha: Int) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.statusBarColor = calculateStatusColor(
//            colorId,
//            statusBarAlpha
//        )
//    }
//}
//
//
//fun Activity.setTranslucent(@IntRange(from = 0, to = 255) statusBarAlpha: Int) {
//    setTransparent()
//    addTranslucentView(statusBarAlpha)
//}
//
//fun Activity.setTransparent() {
//    transparentStatusBar()
//    setRootView()
//}
//
//fun Activity.setRootView() {
//    val parent = findViewById<View>(R.id.content) as ViewGroup
//    var i = 0
//    val count = parent.childCount
//    while (i < count) {
//        val childView = parent.getChildAt(i)
//        if (childView is ViewGroup) {
//            childView.setFitsSystemWindows(true)
//            childView.clipToPadding = true
//        }
//        i++
//    }
//}
//
//fun Activity.transparentStatusBar() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//        window.statusBarColor = Color.TRANSPARENT
//    }
//}
//
//fun Activity.addTranslucentView(
//    @IntRange(from = 0, to = 255) statusBarAlpha: Int
//) {
//    val contentView = findViewById<View>(R.id.content) as ViewGroup
//    val fakeTranslucentView =
//        contentView.findViewById<View>(FAKE_TRANSLUCENT_VIEW_ID)
//    if (fakeTranslucentView != null) {
//        if (fakeTranslucentView.visibility == View.GONE) {
//            fakeTranslucentView.visibility = View.VISIBLE
//        }
//        fakeTranslucentView.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0))
//    } else {
//        contentView.addView(
//            createTranslucentStatusBarView(
//                statusBarAlpha
//            )
//        )
//    }
//}
//
//fun Activity.createTranslucentStatusBarView(alpha: Int): View? {
//    // 绘制一个和状态栏一样高的矩形
//    val statusBarView = View(this)
//    val params = LinearLayout.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        getStatusBarHeight()
//    )
//    statusBarView.layoutParams = params
//    statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0))
//    statusBarView.id = FAKE_TRANSLUCENT_VIEW_ID
//    return statusBarView
//}

fun Activity.getStatusBarHeight(): Int {
    // 获得状态栏高度
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

private fun calculateStatusColor(@ColorInt color: Int, alpha: Int): Int {
    if (alpha == 0) {
        return color
    }
    val a = 1 - alpha / 255f
    var red = color shr 16 and 0xff
    var green = color shr 8 and 0xff
    var blue = color and 0xff
    red = (red * a + 0.5).toInt()
    green = (green * a + 0.5).toInt()
    blue = (blue * a + 0.5).toInt()
    return 0xff shl 24 or (red shl 16) or (green shl 8) or blue
}

object LiveDisplayCutout {

    /**
     * 是否是androidP 刘海屏且已被设置不占用刘海
     * @return
     */
    @JvmStatic
    fun isAndroidPDisplayCutout(mWindow: Window): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER == mWindow.attributes.layoutInDisplayCutoutMode
    }

    @JvmStatic
    fun isSamsungRoundHoleDisplay(mWindow: Window): Boolean {
        return NotchCompat.hasDisplayCutoutHardware(mWindow)
    }

    /**
     * 是否是刘海屏
     * @return
     */
    @JvmStatic
    fun hasDisplayCutout(mWindow: Window): Boolean {
        return NotchCompat.hasDisplayCutout(mWindow)
    }

    /**
     * 凹凸屏判断 android O |Android P |特殊屏幕
     * @return
     */
    @JvmStatic
    fun hasDisplayCutoutAllSituations(window: Window): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hasDisplayCutout(window) ||
                    isAndroidPDisplayCutout(window) ||
                    isSamsungRoundHoleDisplay(window)
        } else false
    }
}

