package com.component.kotlintest.first

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * @Author fox
 * @Date 2020/1/15 22:32
 */


//1.将方法作为map的value 调用时直接使用invoke进行调用
private val keymap: Map<Int, () -> Unit> = mapOf(
    0 to { print(0) },
    1 to { print(1) },
    2 to { print(2) }
)

fun keymapInvoke(key: Int) {
    keymap[key]?.invoke()
}

//2.重载companion object的invoke 简化版的工厂模式
//invoke的含义是将对象作为函数进行调用
interface Iface1<E> : List<E> {
    companion object Factory {
        operator fun <E> invoke(capacity: Int = 0): List<E> {
            return when (capacity) {
                0 -> ArrayList()
                1 -> LinkedList()
                else -> CopyOnWriteArrayList<E>()
            }
        }

        //invoke可以有任意重载方法
        operator fun <E> invoke(name: String): List<E> {
            return ArrayList()
        }
    }

}

val iface1 = Iface1<Any>(1)
val iface2 = Iface1<Any>("hello")

//3.属性委托 数据观察者模式
interface StockUpdateListener {
    fun onRise(price: Int)
    fun onFall(price: Int)
}

class StockDisplay : StockUpdateListener {

    override fun onFall(price: Int) {
        println("The stock price has fell to $price")
    }

    override fun onRise(price: Int) {
        println("The stock price has risen to $price")
    }

}

class StockUpdate {
    val listeners = mutableSetOf<StockUpdateListener>()

    var price: Int by Delegates.observable(0) { _, old, new ->
        listeners.forEach {
            if (new > old) it.onRise(price) else it.onFall(price)
        }
    }
}

//使用vetoable来拦截观察对象
var value: Int by Delegates.vetoable(0) { _, old, new ->
    //与observable不同的是 只有new的值大于0才会被接受 否则会被截获
    new > 0
}

//4.属性可以使用map进行初始化
class User5(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

//5.invoke可以作为必须重写的方法使用
data class Issue(
    val id: String,
    val project: String,
    val type: String,
    val priority: String,
    val description: String
)

class ImportIssuesPredicate(val project: String) : (Issue) -> Boolean {

    override fun invoke(issue: Issue): Boolean {
        return issue.priority == project && issue.isImport()
    }

    private fun Issue.isImport(): Boolean {
        return type == "Bug" && (priority == "Major" || priority == "Critical")
    }
}

//6.内联函数创建抽象工厂
interface Computer

class Dell : Computer
class Asus : Computer
class Acer : Computer

abstract class AbstractFactory {

    abstract fun produce(): Computer

    companion object {
        inline operator fun <reified T : Computer> invoke(): AbstractFactory =
//注意 这个地方是T::class 不是T::class.java
            when (T::class) {
                Dell::class -> DellFactory()
                Asus::class -> AsusFactory()
                Acer::class -> AcerFactory()
                else -> throw IllegalArgumentException()
            }
    }
}

class DellFactory : AbstractFactory() {
    override fun produce(): Computer = Dell()
}

class AsusFactory : AbstractFactory() {
    override fun produce(): Computer = Asus()
}

class AcerFactory : AbstractFactory() {
    override fun produce(): Computer = Acer()
}

//7.lambda实现责任链
class PartialFunction<in P1, out R>(
    private val defineAt: (P1) -> Boolean,
    private val f: (P1) -> R
) : (P1) -> R {
    override fun invoke(p1: P1): R {
        if (defineAt(p1)) {
            return f(p1)
        } else {
            throw java.lang.IllegalArgumentException("Value: ($p1) isn't support by this function")
        }
    }

    fun isDefinedAt(p1: P1) = defineAt(p1)
}

infix fun <P1, R> PartialFunction<P1, R>.orElse(that: PartialFunction<P1, R>): PartialFunction<P1, R> {
    return PartialFunction({
        this.isDefinedAt(it) || that.isDefinedAt(it)
    }) {
        when {
            this.isDefinedAt(it) -> this(it)
            else -> that(it)
        }
    }
}

