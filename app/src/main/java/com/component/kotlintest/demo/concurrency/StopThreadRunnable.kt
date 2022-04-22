package com.component.kotlintest.demo.concurrency

//停止线程的做法
class StopThreadRunnable : Runnable {
    override fun run() {
        var num: Int = 0
        // 在这里加入对isInterrupted的监听 就可以相应外部的interrupt信号
        // 否则可以对外部的interrupt信号忽略
        // 使用此条件 返回2147480000
        // while (num <= Int.MAX_VALUE && num >= 0) {
        // 使用此条件 返回1054600000
        while (!Thread.currentThread().isInterrupted && num <= Int.MAX_VALUE && num >= 0) {
            if (num % 10000 == 0) {
                println("$num 是10000的倍数")
            }
            num++
        }
        println("任务运行结束")
    }
}

fun main() {
    val thread = Thread(StopThreadRunnable())
    thread.start()
    Thread.sleep(2000)
    //这里是通知该线程停止 并不是强制该线程停止
    thread.interrupt()
}