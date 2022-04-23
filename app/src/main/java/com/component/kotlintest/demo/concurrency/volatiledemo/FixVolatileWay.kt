package com.component.kotlintest.demo.concurrency.volatiledemo

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


/**
 * @Author fox
 * @Date 2022/4/23 20:08
 * 使用interrupt来中断生产者、消费者线程
 */
class Producer1(private val storage: BlockingQueue<Int>) : Runnable {
    override fun run() {
        var num = 0
        try {
            while (num <= 100000 && !Thread.currentThread().isInterrupted) {
                if (num % 100 == 0) {
                    storage.put(num)
                    println(num.toString() + "是100的倍数,被放到仓库中了。")
                }
                num++
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            println("生产者结束运行")
        }
    }
}

class Consumer1(val storage: BlockingQueue<Int>) {
    fun needMoreNums(): Boolean {
        return Math.random() <= 0.95
    }
}

fun main() {
    val storage = ArrayBlockingQueue<Int>(10)
    val producer1 = Producer1(storage)
    val producerThread = Thread(producer1)
    producerThread.start()
    Thread.sleep(1000)
    val consumer1 = Consumer1(storage)
    while (consumer1.needMoreNums()) {
        println(consumer1.storage.take().toString() + "被消费了")
        Thread.sleep(100)
    }
    println("消费者不需要更多数据了。")
    producerThread.interrupt()
}