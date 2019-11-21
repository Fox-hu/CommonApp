package com.silver.fox.ext

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

@SuppressLint("MissingPermission")
fun Context.isNetWorkAvailable(): Boolean {
    var manager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var info = manager.activeNetworkInfo
    return !(null == info || !info.isConnected)
}