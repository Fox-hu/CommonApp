package com.component.kotlintest.demo.kotlin_demo.koans

import java.util.*

//task4
fun containsEven(collection: Collection<Int>): Boolean = collection.any { it % 2 == 0 }

//task5
val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"

fun getPattern(): String = """\d{2} $month \d{4}"""

//task6
data class Person(val name: String, val age: Int)

fun getPeople(): List<Person> {
    return listOf(Person("Alice", 29), Person("Bob", 31))
}

//task7
fun sendMessageToClient(
    client: Client?, message: String?, mailer: Mailer
) {
    val email = client?.personalInfo?.email
    if (email != null && message != null) {
        mailer.sendMessage(email, message)
    }

}

class Client(val personalInfo: PersonalInfo?)
class PersonalInfo(val email: String?)
interface Mailer {
    fun sendMessage(email: String, message: String)
}

//task8
fun eval(expr: Expr): Int =
    when (expr) {
        is Num -> expr.value
        is Sum -> eval(expr.left) + eval(
            expr.right
        )
        else -> throw IllegalArgumentException("Unknown expression")
    }

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) :
    Expr

//task9
fun Int.r(): RationalNumber = RationalNumber(this, 1)

fun Pair<Int, Int>.r(): RationalNumber =
    RationalNumber(first, second)

data class RationalNumber(val numerator: Int, val denominator: Int)

//task10
fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    //java方式排序
    Collections.sort(arrayList) { i:Int, j:Int -> j-i}
    //使用kotlin的方式
    //arrayList.sortWith(Comparator { i:Int, j:Int -> j-i})
    return arrayList
}

//task11
fun getDescendList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    arrayList.sortWith(Comparator{i:Int, j:Int -> j-i})
    Person1(name ="lisi")
    Person1(age = 20)
    return arrayList
}

class Person1(var name: String = "lili", var age:Int = 20,var num:Int = 200)


