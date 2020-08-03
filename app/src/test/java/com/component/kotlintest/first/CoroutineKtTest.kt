package com.component.kotlintest.first

import com.component.kotlintest.test.Son
import org.junit.Test

/**
 * @Author fox.hu
 * @Date 2020/4/22 16:15
 */
class CoroutineKtTest {

    @Test
    fun coro1() {
        corotines1()
    }

    @Test
    fun coro3() {
        corotines3()
    }

    @Test
    fun TestOrder(){
        Son()
    }
}