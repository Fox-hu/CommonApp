package com.component.kotlintest.first

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

class Bird1(flyer: Flyer, animal: Animal) : CanFly by flyer, CanEat by animal {

}

fun main(args: Array<String>) {
    val flyer = Flyer()
    val animal = Animal()
    val bird1 = Bird1(flyer, animal)
    bird1.eat()
    bird1.fly()
}