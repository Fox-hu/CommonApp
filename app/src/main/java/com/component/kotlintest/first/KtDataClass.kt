package com.component.kotlintest.first

//一行代码自动有get set toString hashcode equals方法等
data class Bird2(var weight: Double, var age: Int, var color: String)

//利用pair和triple类和解构方法，来进行复制操作
fun main() {
    val (weightP, ageP) = Pair(20.0, 1)
    val (weightT, ageT, colorT) = Triple(20.1, 2, "green")
}