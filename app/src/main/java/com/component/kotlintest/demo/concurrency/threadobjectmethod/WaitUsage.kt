package com.component.kotlintest.demo.concurrency.threadobjectmethod

/**
 * @Author fox
 * @Date 2022/4/28 23:49
 * 展示wait和notify的基本用法 1. 研究代码执行顺序 2. 证明wait释放锁
 */
class WaitUsage {
    companion object {
        val lock: Object = Object()
    }

    class WaitThread1: Thread() {
        override fun run() {
            synchronized(lock){
                println("线程：${currentThread().name} 开始执行")
                //释放了锁资源
                println("线程：${currentThread().name} 调用wait")
                //调用wait后 线程暂停 等待lock.notify/notifyAll后继续执行
                lock.wait()
                println("线程：${currentThread().name} 获取到了锁 继续执行")
            }
        }
    }

    class WaitThread2:Thread(){
        override fun run() {
            synchronized(lock){
                println("线程：${currentThread().name} 开始执行")
                lock.notify()
                println("线程：${currentThread().name} 调用notify")
            }
        }
    }
}

fun main() {
    val thread1 = WaitUsage.WaitThread1()
    val thread2 = WaitUsage.WaitThread2()
    thread1.start()
    Thread.sleep(200)
    thread2.start()
}