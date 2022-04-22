package com.component.kotlintest.demo.kotlin_demo

class Bird(weight: Double, age: Int, color: String) {
    val weight: Double
    val age: Int
    val color: String

    //by lazy 延迟初始化
    //只能用于val上 默认的PUBLICATION保证它是线程安全的,而NONE参数表示不进行任何线程保护
    val sex: String by lazy(LazyThreadSafetyMode.PUBLICATION) {
        if (color == "yello") "male" else "famale"
    }

    //主从构造方法演示
    constructor(double: Double) : this(double, 0, "white")

    constructor(double: Double, int: Int) : this(double, int, "white")

    //lateinit只能用于var变量 而且不能是基本类型，如果是基本类型则需要使用包装类
    lateinit var type: String

    //构造方法可以拥有多个init方法，会从上往下依次执行
    init {
        this.weight = weight
        println("the bird's weight is $weight")
        this.age = age
        println("the bird's age is $age")
    }

    init {
        this.color = color
        println("the color's age is $color")
    }

    fun fly() {}

}

//open关键字代表可以被继承
open class Test {
    fun test() {
        val bird = Bird(100.0, 1, "yello")
    }
}