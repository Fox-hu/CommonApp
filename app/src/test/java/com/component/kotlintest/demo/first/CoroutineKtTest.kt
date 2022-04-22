package com.component.kotlintest.demo.first

import com.component.kotlintest.demo.kotlin_demo.corotines1
import com.component.kotlintest.demo.kotlin_demo.corotines3
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

    class Student(var name:String, var age:Int)

    @Test
    fun test() {
        val student = Student("Bobo", 15)
        changeValue1(student) // student值未改变，不为null! 输出结果 student值为 name:Bobo、age:15
        // changeValue2(student);  // student值被改变，输出结果 student值为 name:Lily、age:20
        System.out.println("student值为 name: " + student.name.toString() + "、age:" + student.age)
    }

    private fun changeValue1(student: Student?) {
        var student: Student? = student
        student = null
    }

    fun changeValue2(student: Student) {
        student.name = "Lily"
        student.age = 20
    }
}