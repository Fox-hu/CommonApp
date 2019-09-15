package com.component.kotlintest

import org.junit.Test

class Test {

    fun foo(int:Int){
        print(int)
    }

    @Test
    fun test1() {
        listOf<Int>(1,2,3).forEach { foo(it) }
    }
}