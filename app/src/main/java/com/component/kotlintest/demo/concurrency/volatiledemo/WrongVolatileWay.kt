package com.component.kotlintest.demo.concurrency.volatiledemo

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue


/**
 * @Author fox
 * @Date 2022/4/23 19:33
 * 这个案例演示了 为什么不推荐使用volatile的方式来中断线程 因为volatile无法响应阻塞时中断
 */
class Producer(private val storage: BlockingQueue<Int>) : Runnable {
    @Volatile
    var canceled = false
    override fun run() {
        var num = 0
        try {
            while (num <= 100000 && !canceled) {
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
class Consumer(val storage: BlockingQueue<Int>) {
    fun needMoreNums(): Boolean {
        return Math.random() <= 0.95
    }
}

fun main() {
    val storage = ArrayBlockingQueue<Int>(10)
    val producer = Producer(storage)
    val producerThread = Thread(producer)
    producerThread.start()
    Thread.sleep(1000)
    val consumer = Consumer(storage)
    while (consumer.needMoreNums()) {
        println(consumer.storage.take().toString() + "被消费了")
        Thread.sleep(100)
    }
    println("消费者不需要更多数据了。")
    //一旦消费不需要更多数据了，我们应该让生产者也停下来，但是实际情况并没有停下来
    producer.canceled = true
    println(producer.canceled)
}
