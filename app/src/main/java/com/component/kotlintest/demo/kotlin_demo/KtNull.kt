package com.component.kotlintest.demo.kotlin_demo

import java.io.File

//kotlin中都是默认不为空的 如果可以为空则在后面添加？
class Seat(file: File?) {
    //If not null and else 缩写
    fun null1() {
        val files = File("test").listFiles()
        println(files?.size ?: "empty")
    }

    fun null2() : String{
        val files = File("test").listFiles()
        val defaultValue = "defaultValue"
        files?.let {
            // 假如files不为null代码会执⾏到此处,
            return "not null"
        } ?: defaultValue
            // 假如files为null代码会返回defaultValue
        return defaultValue
    }

}