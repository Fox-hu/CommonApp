package com.component.kotlintest.first

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

/**
 * @Author fox
 * @Date 2020/1/15 22:32
 */
class KotlinSkill {

    //将方法作为map的value 调用时直接使用invoke进行调用
    private val keymap: Map<Int, () -> Unit> = mapOf(
        0 to { print(0) },
        1 to { print(1) },
        2 to { print(2) }
    )

    fun keymapInvoke(key: Int) {
        keymap[key]?.invoke()
    }
}

//重载companion object的操作符 简化版的工厂模式
interface Iface1<E> : List<E> {
    companion object Factory {
        operator fun <E> invoke(capacity: Int = 0): List<E> {
            return when (capacity) {
                0 -> ArrayList()
                1 -> LinkedList()
                else -> CopyOnWriteArrayList<E>()
            }
        }
    }

}

val iface1 = Iface1<Any>(1)

//属性委托 数据观察者模式
class User4 {
    var name: String by Delegates.observable("no name") { d, old, new ->
        println("$old - $new")
    }
}

//属性可以使用map进行初始化
class User5(val map: Map<String, Any?>) {
    val name: String by map
    val age:Int by map
}

