package com.silver.fox.ext

import android.content.Context
import android.widget.Toast
import com.silver.fox.common.InitApp

fun String.showShortToast(context: Context = InitApp.CONTEXT){
    Toast.makeText(context, this, Toast.LENGTH_SHORT).apply {
        show()
    }
}

fun String.showLongToast(context: Context = InitApp.CONTEXT){
    Toast.makeText(context, this, Toast.LENGTH_LONG).apply {
        show()
    }
}