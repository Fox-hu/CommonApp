package com.component.kotlintest.demo.concurrency.threadobjectmethod

/**
 * @Author fox
 * @Date 2022/5/4 11:25
 * 两个线程交替打印0~100的奇偶数 用wait和notify来实现
 */
class WaitNotifyPrintOddEve : Runnable {
    val lock = Object()
    var count = 0
    override fun run() {
        while (count <= 100) {
            synchronized(lock) {
                //1、拿到锁我们就打印
                println("${Thread.currentThread().name} : ${count++}")
                //2、打印完 唤醒其他线程 自己就休眠
                lock.notify()
                if (count <= 100) {
                    lock.wait()
                }
            }
        }
    }
}

fun main() {
    val runnable = WaitNotifyPrintOddEve()
    Thread(runnable,"偶数线程").start()
    Thread(runnable,"奇数线程").start()
}