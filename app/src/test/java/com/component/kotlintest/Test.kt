package com.component.kotlintest

import com.component.kotlintest.first.KotlinSkill
import com.component.kotlintest.first.User4
import com.component.kotlintest.first.User5
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
        val kotlinSkill = KotlinSkill()
        kotlinSkill.keymapInvoke(1)
    }

    @Test
    fun test4() {
        val user4 = User4()
        user4.name = "fox"
    }

    @Test
    fun test5() {
        val user5 = User5(mapOf("name" to "fox", "age" to 29))
        println("name = ${user5.name},age = ${user5.age}")
    }
}