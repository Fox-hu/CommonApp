package com.component.kotlintest.demo.concurrency.threadstates

/**
 * @Author fox
 * @Date 2022/4/23 22:24
 * 展示线程的new、runnable、terminated状态
 * 即使是正在运行 也是runnable状态 不是running
 */
class NewRunnableTerminated : Runnable {
    override fun run() {
        for (i in 0..1000) {
            println("i = $i")
        }
    }
}

fun main() {
    val thread = Thread(NewRunnableTerminated())
    println("thread states = ${thread.state}")

    thread.start()
    println("thread states = ${thread.state}")

    Thread.sleep(10)
    println("thread states = ${thread.state}")

    Thread.sleep(100)
    println("thread states = ${thread.state}")
}