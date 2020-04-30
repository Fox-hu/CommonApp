package com.component.kotlintest.first

import org.junit.Test

/**
 * @Author fox.hu
 * @Date 2020/4/23 16:33
 */
class DSLKtTest {

    @Test
    fun kotlinDSL() {
        kotlinDSL {
            append("DSL")
            println(this)
        }
    }
}