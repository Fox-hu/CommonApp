package com.component.kotlintest.first

import kotlin.reflect.KProperty

interface CanFly {
    fun fly()
}

interface CanEat {
    fun eat()
}

open class Flyer : CanFly {
    override fun fly() {
        println("I can fly")
    }
}

open class Animal : CanEat {
    override fun eat() {
        println("I can eat")
    }
}

//by关键字指定委托类
class Bird1(canFly: CanFly, canEat: CanEat) : CanFly by canFly, CanEat by canEat

fun main() {
    val flyer = Flyer()
    val animal = Animal()
    val bird1 = Bird1(flyer, animal)
    //bird对象指定了委托类，所以可以直接调用eat() fly()方法
    bird1.eat()
    bird1.fly()

    //委托方法
    val e = Example()
    println(e.p)
}

//委托属性
class Example {
    var p: String by Delegate()
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}
