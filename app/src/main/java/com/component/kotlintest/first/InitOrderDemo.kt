package com.component.kotlintest.first

class InitOrderDemo(name: String) {
    val firstProperty = "First property:$name".also(::println)

    init {
        println("First initializer block that prints $name")
    }

    val secondProperty = "Second property:$name".also(::println)

    init {
        println("Second initializer block that prints $name")
    }
}

class InitOrderDemo1(name: String) {
    val firstProperty = "First property:$name".also(::println)

    init {
        println("First initializer block that prints $name")
    }

    init {
        println("Second initializer block that prints $name")
    }

    val secondProperty = "Second property:$name".also(::println)
}

fun main() {
    InitOrderDemo("hello world")
    InitOrderDemo1("hello world")
}
