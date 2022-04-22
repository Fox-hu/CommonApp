package com.component.kotlintest.demo.kotlin_demo

import kotlinx.coroutines.*

/**
 * @Author fox.hu
 * @Date 2020/4/22 16:04
 */

fun corotines1() {
    GlobalScope.launch {
        delay(1000)
        println("World")
    }

    println("Hello")
//会阻塞线程
    runBlocking {
        delay(2000)
    }
}

fun corotines2() {
    runBlocking {
        launch {
            delay(200)
            println("Task from runBlocking")
        }

        coroutineScope {
            launch {
                delay(500)
                println("Task from nested launch")
            }

            delay(100)
            println("Task from coroutine scope")
        }

    }
    println("Coroutine scope is over")
}

suspend fun doSomethingOne(): Int {
    delay(1000)
    return 13
}

suspend fun doSomethingTwo(): Int {
    delay(1000)
    return 29
}

fun corotines3() {
    suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingOne() }
        val two = async { doSomethingTwo() }
        one.await() + two.await()
    }
}


