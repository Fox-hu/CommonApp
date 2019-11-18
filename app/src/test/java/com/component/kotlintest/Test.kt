package com.component.kotlintest

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
}