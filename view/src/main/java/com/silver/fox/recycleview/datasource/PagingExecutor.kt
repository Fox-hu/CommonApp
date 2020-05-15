package com.silver.fox.recycleview.datasource

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * @Author fox.hu
 * @Date 2020/5/15 13:52
 */
object PagingExecutor {
    private val IO_THREAD_POOL = Executors.newFixedThreadPool(2)

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    val MAIN_THREAD_EXECUTOR = Executor { command ->
            executeOnMainThread(command)
        }

    val IO_THREAD_EXECUTOR = Executor { command ->
        executeOnMainThread(command)
    }

    private fun executeOnIo(runnable: Runnable){
        IO_THREAD_POOL.execute(runnable)
    }

    private fun executeOnMainThread(runnable: Runnable) {
        if (isMainThread) {
            runnable.run()
        } else {
            postToMainThread(runnable)
        }
    }

    private fun postToMainThread(runnable: Runnable) {
        mainHandler.post(runnable)
    }

    private val isMainThread
        get() = Looper.getMainLooper().thread == Thread.currentThread()
}