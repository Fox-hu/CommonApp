package com.component.kotlintest.demo.concurrency.threadobjectmethod

/**
 * @Author fox
 * @Date 2022/5/4 14:14
 */

fun main() {
    val thread1 = Thread{
        Thread.sleep(1000)
        println("子线程1执行完毕")
    }

    val thread2 = Thread{
        Thread.sleep(1000)
        println("子线程2执行完毕")
    }

    thread1.start()
    thread2.start()
    println("开始等待子线程执行完毕")
    //join后，main线程要等待子线程执行完毕后才能执行
    thread1.join()
    thread2.join()
    println("所有子线程执行完毕")
}