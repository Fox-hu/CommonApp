package com.silver.fox.ext

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.FutureTask

fun Handler.invoke(runnable: () -> Unit) {
    if (Looper.myLooper() == this.looper) {
        runnable()
    } else {
        try {
            val task = FutureTask<Void>(runnable, null)
            post(task)
            task.get()
        } catch (e: Exception) {
            Log.e(TAG, "Handler.invoke, throwable = $e")
        }
    }
}

fun Handler.fastInvoke(runnable: () -> Unit) {
    if (Looper.myLooper() == this.looper) {
        runnable()
    } else {
        try {
            val task = FutureTask<Void>(runnable, null)
            postAtFrontOfQueue(task)
            task.get()
        } catch (e: Exception) {
            Log.e(TAG, "Handler.invoke, throwable = $e")
        }
    }
}

val Throwable.ktMessage: String
    get() = message ?: "(null)"