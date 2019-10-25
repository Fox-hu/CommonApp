package com.component.kotlintest.extensions

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView

//扩展属性
val View.ctx: Context get() = context

//扩展方法
fun View.sildeExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.sildeEnter() {
    if (translationY < 0) animate().translationY(0f)
}

var TextView.textColor: Int
    get() = currentTextColor
    set(value) = setTextColor(value)

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)