package com.component.kotlintest.demo.concurrency.stopthread


/**
 * @Author fox
 * @Date 2022/4/23 18:04
 * 中断线程的最佳实践
 * 示例：在5s内 从0开始每隔10ms+1,打印出所有10的倍数的值
 */
class BestStopThreadWay : Runnable {
    override fun run() {
        var num = 0
        while (true) {
            // 监听isInterrupted状态 如果监听到了则中断线程
            if (Thread.currentThread().isInterrupted) {
                println("interrupt 线程中断")
                break
            }
            try {
                //执行业务逻辑
                if (num % 10 == 0 && num < Int.MAX_VALUE) {
                    println("$num 是10的倍数")
                }
                num++
                Thread.sleep(10)
            } catch (e: InterruptedException) {
                // 如果在sleep时接收到interrupt信息 则会走到此catch中
                // 这时interrupt信息会被清空 需要再次调用interrupt 在正常流程中中断线程
                e.printStackTrace()
                Thread.currentThread().interrupt()
            }
        }
    }
}

fun main() {
    val thread = Thread(BestStopThreadWay())
    thread.start()
    Thread.sleep(5000)
    thread.interrupt()
}