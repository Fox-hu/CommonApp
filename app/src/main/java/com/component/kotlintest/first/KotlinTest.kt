package com.component.kotlintest.first

class KotlinTest {
    val a: String = "hello world"

    //声明一个方法
    fun sum(x: Int, y: Int): Int {
        return x + y
    }

    //代码块函数体
    fun sum1(x: Int, y: Int): Int = x + y

    //val 和var的区别 val = var + final
    val x = intArrayOf(1, 3, 2)

    //kotlin中的函数是头等公民
    //它允许以其他函数作为参数或返回值的函数
    fun foo(x: Int) {
        fun double(y: Int): Int {
            return y * 2
        }
        println(double(x))
    }
}