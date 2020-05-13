package com.silver.fox.ext

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.silver.fox.Ktx

@ColorInt
fun Int.getColor(context: Context = Ktx.app): Int = ContextCompat.getColor(context, this)


fun Int.getString(context: Context = Ktx.app): String = context.getString(this)

fun Int.getStringArray(context: Context = Ktx.app): Array<String> =
    context.resources.getStringArray(this)