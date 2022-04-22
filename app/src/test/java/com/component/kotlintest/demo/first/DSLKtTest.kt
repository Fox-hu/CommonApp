package com.component.kotlintest.demo.first

import org.junit.Test

/**
 * @Author fox.hu
 * @Date 2020/4/23 16:33
 */
class DSLKtTest {

    @Test
    fun kotlinDSL() {
        com.component.kotlintest.demo.kotlin_demo.kotlinDSL {
            append("DSL")
            println(this)
        }
    }
}