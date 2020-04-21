package com.component.kotlintest

import com.component.kotlintest.first.*
import org.junit.Test

class Test {

    fun foo(int: Int) {
        print(int)
    }

    @Test
    fun test1() {
        listOf<Int>(1, 2, 3).forEach { foo(it) }
    }

    @Test
    fun test2() {
        val list = listOf<Int>(1, 2, 3, 4, 5, 6, 7)
        val filter = list.filter { it % 2 == 0 }
        System.out.print(filter)
    }

    @Test
    fun test3() {
        keymapInvoke(1)
    }

    @Test
    fun test4() {
        val stockUpdate = StockUpdate()
        val stockDisplay = StockDisplay()
        stockUpdate.listeners.add(stockDisplay)
        stockUpdate.price = 100
        stockUpdate.price = 98
    }

    @Test
    fun test5() {
        val user5 = User5(mapOf("name" to "fox", "age" to 29))
        println("name = ${user5.name},age = ${user5.age}")
    }


    @Test
    fun test6() {
        val abstractFactory = AbstractFactory<Dell>()
        print(abstractFactory.produce())
    }

    @Test
    fun test7() {
        //好好理解这个
        data class ApplyEvent(val money: Int, val title: String)

        val groupLeader = {
            val defineAt: (ApplyEvent) -> Boolean = { it.money <= 200 }
            val handler: (ApplyEvent) -> Unit =
                { println("GroupLeader handled application:${it.title}") }
            PartialFunction(defineAt, handler)
        }()

        val president = {
            val defineAt: (ApplyEvent) -> Boolean = { it.money <= 500 }
            val handler: (ApplyEvent) -> Unit =
                { println("President handled application:${it.title}") }
            PartialFunction(defineAt, handler)
        }()

        val college = {
            val defineAt: (ApplyEvent) -> Boolean = { true }
            val handler: (ApplyEvent) -> Unit =
                {
                    when {
                        it.money > 1000 -> println("College: This application is refused.")
                        else -> println("College handled application:${it.title}")
                    }
                }
            PartialFunction(defineAt, handler)
        }()

        val applyChain = groupLeader orElse president orElse college
        applyChain(ApplyEvent(2000, "hold a debate match"))
    }
}