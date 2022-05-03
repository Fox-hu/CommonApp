package com.component.kotlintest.demo.concurrency.threadobjectmethod

import java.util.*

/**
 * @Author fox
 * @Date 2022/4/29 10:13
 */

class Produce(private val storage: Storage) : Runnable {
    override fun run() {
        for (i in 1..100) {
            println("生产循环 第 $i 次")
            storage.put()
        }
    }
}

class Consumer(private val storage: Storage) : Runnable {
    override fun run() {
        for (i in 1..100) {
            println("消费循环 第 $i 次")
            storage.take()
        }
    }
}

class Storage {
    private val lock = Object()
    private val maxSize = 10
    private val storageList = LinkedList<Any>()

    fun put() {
        synchronized(lock) {
            if (storageList.size == maxSize) {
                lock.wait()
            }
            storageList.add(Any())
            println("生产者 生产了商品 ,仓库里有了 ${storageList.size}个产品")
            lock.notify()
        }
    }

    fun take() {
        synchronized(lock) {
            if (storageList.isEmpty()) {
                lock.wait()
            }
            storageList.poll()
            println("消费者 消费了商品 ,现在仓库还剩下 ${storageList.size}")
            lock.notify()
        }
    }
}

fun main() {
    val storage = Storage()
    val producer = Produce(storage)
    val consume = Consumer(storage)
    Thread(producer).start()
    Thread(consume).start()
}