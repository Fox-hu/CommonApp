package com.component.kotlintest.extensions

import android.content.Context
import android.view.View

val View.ctx: Context get() = context

fun View.sildeExit() {
    if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.sildeEnter() {
    if (translationY < 0) animate().translationY(0f)
}