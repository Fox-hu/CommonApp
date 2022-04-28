package com.component.kotlintest.demo.concurrency.threadobjectmethod

/**
 * @Author fox
 * @Date 2022/4/29 00:06
 */

val lock = Object()

class WaitNotifyAll :Runnable{
    override fun run() {
        synchronized(lock){
            println("线程：${Thread.currentThread().name} 开始执行")
            println("线程：${Thread.currentThread().name} wait开始执行")
            lock.wait()
            println("线程：${Thread.currentThread().name} 重新执行")
//            lock.notify()
        }
    }
}

fun main() {
    val r = WaitNotifyAll()
    val thread1 = Thread(r)
    val thread2 = Thread(r)
    val thread3 = Thread {
        synchronized(lock){
            println("线程：${Thread.currentThread().name} 开始执行")
            lock.notifyAll() //notifyAll将通知所有使用lock对象wait的线程锁释放了
            //lock.notify() //将随机通知一个使用lock对象wait的线程锁释放了 其余的线程则继续wait
            println("线程：${Thread.currentThread().name} 调用notifyAll")
        }
    }
    thread1.start()
    thread2.start()
    Thread.sleep(200)
    thread3.start()
}