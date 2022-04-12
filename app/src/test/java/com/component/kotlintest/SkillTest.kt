package com.component.kotlintest

import com.component.kotlintest.first.*
import com.component.kotlintest.java.Son
import org.junit.Test

class SkillTest {

    fun foo(int: Int) {
        print(int)
    }

    @Test
    fun test1() {
        listOf<Int>(1, 2, 3).forEach { foo(it) }
    }

    @Test
    fun testLambdaMap() {
        keymapInvoke(1)
    }

    @Test
    fun testObserver() {
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
    fun testAbstractFactory() {
        val dell = AbstractFactory<Dell>()
        print(dell.produce())
        val asus = AbstractFactory<Asus>()
        print(asus.produce())
        val acer = AbstractFactory<Acer>()
        print(acer.produce())
    }

    @Test
    fun testResponseListChain() {
        //好好理解这个
        data class ApplyEvent(val money: Int, val title: String)

        val groupLeader = PartialFunction<ApplyEvent, Unit>(defineAt = { it.money <= 200 }) {
            println("GroupLeader handled application:${it.title}")
        }

        val president = PartialFunction<ApplyEvent, Unit>(defineAt = { it.money <= 500 }) {
            println("President handled application:${it.title}")
        }


        val college = PartialFunction<ApplyEvent, Unit>(defineAt = { true }) {
            when {
                it.money > 1000 -> println("College: This application is refused.")
                else -> println("College handled application:${it.title}")
            }
        }
        val applyChain = groupLeader orElse president orElse college
        applyChain(ApplyEvent(2000, "hold a debate match"))
    }

    @Test
    fun testDecorate() {
        Printer().run {
            decorate {
                drawStars()
            }
            decorate {
                drawDottedLine()
            }
            decorate {
                drawLine()
            }
        }
    }


    @Test
    fun testDSL() {
        print(html {
            head {
                title { +"HTML encoding with Kotlin" }
            }
        })
    }

    @Test
    fun TestOrder(){
        Son()
    }


}