package com.component.kotlintest.demo.concurrency.stopthread

//停止线程的做法 不涉及sleep\wait等阻塞状态 比较理想 平常不会这么简单
class StopThreadWithoutSleep : Runnable {
    override fun run() {
        var num: Int = 0
        // 在这里加入对isInterrupted的监听 就可以相应外部的interrupt信号
        // 否则可以对外部的interrupt信号忽略
        // 使用此条件 返回2147480000
        // while (num < Int.MAX_VALUE) {
        // 使用此条件 返回1054600000
        while (!Thread.currentThread().isInterrupted && num < Int.MAX_VALUE) {
            if (num % 10000 == 0) {
                println("$num 是10000的倍数")
            }
            num++
        }
        println("任务运行结束")
    }
}

//示例 如果发送interrupt 停止线程时 线程正处于 sleep或wait 等阻塞状态
//解决方案是双interrupt 在线程外使用interrupt方法发送中断信息
//在线程内部捕获interrupt的catch，重新发送interrupt信息 在正常流程中终止线程
class StopThreadWithSleep : Runnable {
    override fun run() {
        var num: Int = 0
        try {
            while (!Thread.currentThread().isInterrupted && num <= 300) {
                if (num % 100 == 0) {
                    println("$num 是100的倍数")
                }
                num++
            }
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            // 这里是在sleep时 接受到了interrupt信息，此时进入catch且清空interrupt标记
            // 所以需要在catch中重新发送interrupt信息 正常流程接受到interrupt信息后终止线程
            Thread.currentThread().interrupt()
            e.printStackTrace()
            println("任务运行结束")
        }
    }
}

fun main() {
    // 无sleep情况下的线程中断
//    val thread = Thread(StopThreadWithoutSleep())
//    thread.start()
//    Thread.sleep(2000)
//    //这里是通知该线程停止 并不是强制该线程停止
//    thread.interrupt()

    // 有sleep的情况下线程中断
    val thread1 = Thread(StopThreadWithSleep())
    thread1.start()
    Thread.sleep(500)
    //这里是通知该线程停止 并不是强制该线程停止
    thread1.interrupt()
}
