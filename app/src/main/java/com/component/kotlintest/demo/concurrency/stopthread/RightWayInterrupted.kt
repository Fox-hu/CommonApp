package com.component.kotlintest.demo.concurrency.stopthread

/**
 * @Author fox
 * @Date 2022/4/23 19:57
 * 注意Thread.interrupted()方法的目标对象是“当前线程”，而不管本方法来自于哪个对象
 */
fun main() {
    val threadOne = Thread {
        while (true) {
        }
    }
    // 启动线程
    threadOne.start()
    //设置中断标志
    threadOne.interrupt()
    //获取中断标志 threadOne
    println("isInterrupted: " + threadOne.isInterrupted)
    //获取中断标志并重置 主线程
    println("isInterrupted: " + Thread.interrupted())
    //获取中断标志并重直 主线程
    println("isInterrupted: " + Thread.currentThread().isInterrupted)
    //获取中断标志 threadOne
    println("isInterrupted: " + threadOne.isInterrupted)
    threadOne.join()
    println("Main thread is over.")
}
