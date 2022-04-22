package com.component.kotlintest.demo.design_patterns

/**
 * @Author fox.hu
 * @Date 2020/1/17 17:34
 */
interface Person {
    fun dressed()
}

abstract class PersonCloth(private val person: Person) : Person {

    override fun dressed() {
        person.dressed()
    }
}

class Boy : Person {
    override fun dressed() {
        println("穿了内衣内裤")
    }
}

class ExpensiveCloth(person: Person) : PersonCloth(person) {
    private fun dressShirt() {
        println("穿衬衫")
    }

    private fun dressCoat() {
        println("穿外套")
    }

    private fun dressJean() {
        println("穿牛仔裤")
    }

    override fun dressed() {
        super.dressed()
        dressShirt()
        dressJean()
        dressJean()
    }
}

class CheapCloth(person: Person) : PersonCloth(person) {
    private fun dressShorts() {
        println("穿条短裤")
    }

    override fun dressed() {
        super.dressed()
        dressShorts()
    }
}

fun main() {
    val boy = Boy()
    val cheapCloth = CheapCloth(boy)
    cheapCloth.dressed()

    val expensiveCloth = ExpensiveCloth(boy)
    expensiveCloth.dressed()
}
