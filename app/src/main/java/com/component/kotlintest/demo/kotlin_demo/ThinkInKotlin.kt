package com.component.kotlintest.demo.kotlin_demo

import java.util.*
import kotlin.reflect.KProperty

class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}

enum class Color {
    RED, ORANGE, YELLOW, GREEN, BLUE, INDIGO, VIOLET
}

fun getMnemonic(color: Color) =
    when (color) {
        Color.RED -> "RED"
        Color.ORANGE -> "ORANGE"
        Color.YELLOW -> "YELLOW"
        Color.GREEN -> "GREEN"
        Color.BLUE -> "BLUE"
        Color.INDIGO -> "INDIGO"
        Color.VIOLET -> "VIOLET"
    }

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold"
}

fun getOther(color: Color) = when (color) {
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    else -> "cold"
}

fun mix(c1: Color, c2: Color) {
    when (setOf<Color>(c1, c2)) {
        setOf(Color.RED, Color.YELLOW) -> "ORANGE"
        setOf(Color.YELLOW, Color.BLUE) -> "GREEN"
        setOf(Color.BLUE, Color.VIOLET) -> "INDIGO"
        else -> throw Exception("Dirty color")
    }
}

fun mixOptimized(c1: Color, c2: Color) {
    when {
        (c1 == Color.RED && c2 == Color.YELLOW) -> "ORANGE"
        (c1 == Color.YELLOW && c2 == Color.BLUE) -> "GREEN"
        (c1 == Color.BLUE && c2 == Color.VIOLET) -> "INDIGO"
        else -> throw Exception("Dirty color")
    }
}

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz"
    i % 3 == 0 -> " Fizz "
    i % 5 == 0 -> "Buzz "
    else -> "$i "
}

fun testFor() {
    for (i in 1..100) {
        print(fizzBuzz(i))
    }
}
// return 1 2 Fizz 4 Buzz Fizz 7

val list = arrayListOf("10", "11", "1001")

fun listTest() {
    for ((index, element) in list.withIndex()) {
        println("$index: $element")
    }
}
//0: 10
//1: 11
//2: 1001


val binaryReps = TreeMap<Char, String>()

fun mapTest() {
    //字符区间迭代 从a到f之间的所有字符
    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        //根据键c将值存到map中 同java中binaryReps.put(c,binary)方法
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        //迭代map，将键值赋值给letter、binary变量
        println("$letter = $binary")
    }
}

fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)
    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

class Person(var name: String = "lili", var age: Int = 20, var num: Int = 100)

fun String.lastChar(): Char = this.get(this.length - 1)

fun StringTest() {
    println("Kotlin".lastChar())
    //return n
}

val String.lastChar: Char
    get() = get(length - 1)

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value) {
        setCharAt(length - 1, value)
    }

fun StringBuilderTest() {
    println("Kotlin".lastChar)
    //return n

    val sb = StringBuilder("kotlin?")
    println(sb.lastChar)
    //return ?
    sb.lastChar = '!'
    println(sb)
    //return kotlin!
}

class User(var id: Int, var name: String, var address: String)

fun saveUser(user: User) {
    if (user.name.isEmpty()) {
        throw IllegalArgumentException(
            "Cannot save user ${user.id}: Name is empty"
        )
    }
    if (user.address.isEmpty()) {
        throw
        IllegalArgumentException(
            "Cannot save user ${user.id}: Address is empty"
        )
    }
}

fun saveUser1(user: User) {
    fun validate(
        user: User,
        value: String,
        fieldName: String
    ) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Cannot save user ${user.id}: $fieldName is empty"
            )
        }
    }
    validate(user, user.name, "Name")
    validate(user, user.address, "Address")
}

fun saveUser2(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${user.id}: " +
                        "$fieldName is empty"
            )
        }
    }
    validate(user.name, "Name")
    validate(user.address, "Address")
}

interface InterfaceTest {
    val user: User

    var userName: String
        get() = user.name
        set(value) {
            user.name = value
        }
}

class interfaceImpl1 : InterfaceTest {
    override val user: User
        get() = User(1,"张三","火星")
}

class interfaceImpl2 : InterfaceTest {
    override val user: User
        get() = User(2,"李四","地球")
}

class User1(val nickname: String)

class User2 constructor(_nickname: String) {
    val nickname: String

    init {
        nickname = _nickname
    }
}

class User3(_nickname: String) {
    val nickname = _nickname
}

class DelegatingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll(elements: Collection<T>): Boolean =
        innerList.containsAll(elements)
}

var ret = DelegatingCollection<String>().size
var ret1 = DelegatingCollection<String>().isEmpty()
var ret2 = DelegatingCollection<String>().contains("hhh")

class DelegatingCollection1<T>(
    innerList: Collection<T> = ArrayList<T>()
) : Collection<T> by innerList

var ret3 = DelegatingCollection1<String>().size
var ret4 = DelegatingCollection1<String>().isEmpty()
var ret5 = DelegatingCollection1<String>().contains("hhh")

fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}


fun findTheOldestWithLambda(people: List<Person>) {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    println(people.maxBy({ person: Person -> person.age }))

    //如果lambda表达式是函数调用的最后一个实参，它可以放到括号的外边去

    println(people.maxBy() { person: Person -> person.age })

    //如果lambda是函数的唯一实参时。可以去掉调用代码中的空括号对
    println(people.maxBy { person: Person -> person.age })

    //如果可以推导出参数类型，就不需要显式的指定它
    //这个it 是自动生成的参数名称 类型是Person
    println(people.maxBy { it.age })
}

//返回a到z的字母表
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }
    result.append("\nNow I know the alphabet!")
    return result.toString()
}

fun alphabetUsingWith() = with(StringBuilder()) {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
    toString()
}

fun alphabetUsingApply() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\nNow I know the alphabet!")
}.toString()


class Foo {
    var p: Person by PersonDelegate()
}

class PersonDelegate<T> {
    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        value ?: throw IllegalStateException("${property.name} not initialized")

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }

}

class MapByTest() {
    private val attributes = hashMapOf<String, String>()

    fun setAttributes(key: String, value: String) {
        attributes[key] = value
    }

    //name这一属性会在map中查找key为map的值
    val name by attributes
}

//高阶函数，以callback函数作为参数
//同时 callback有默认实现
fun <T> lambdaTest1(url: String, callback: (T) -> String = {it.toString()}) {

}