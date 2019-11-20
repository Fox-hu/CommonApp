package com.silver.fox.ext

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.silver.fox.Ktx

@ColorInt
fun Int.getColor(): Int = getColor(Ktx.app)

@ColorInt
fun Int.getColor(context: Context): Int = ContextCompat.getColor(context, this)

fun Int.getString(): String = getString(Ktx.app)

fun Int.getString(context: Context) = context.getString(this)