package com.silver.fox.media.rtmp

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object LiveTaskManager {
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    private val CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4))
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1
    private const val KEEP_ALIVE_SECONDS = 30
    private val sPoolWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue(5)
    private val THREAD_POOL_EXECUTOR: ThreadPoolExecutor

    init {
        THREAD_POOL_EXECUTOR = ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_SECONDS.toLong(), TimeUnit.SECONDS,
            sPoolWorkQueue
        ).apply {
            allowCoreThreadTimeOut(true)
        }

    }

    fun execute(runnable: Runnable?) {
        THREAD_POOL_EXECUTOR.execute(runnable)
    }
}