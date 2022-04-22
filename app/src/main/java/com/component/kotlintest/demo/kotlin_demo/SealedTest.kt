package com.component.kotlintest.demo.kotlin_demo

sealed class SealedTest{
    class Sealed1(): SealedTest()
    class Sealed2(): SealedTest()
}

fun eval(sealedTest: SealedTest){
    when (sealedTest){
        is SealedTest.Sealed1 -> println("Sealed1")
        is SealedTest.Sealed2 -> println("Sealed2")
    }
}