package com.component.kotlintest.first

class Prize(val name: String = "hhh", val count: Int = 123, val type: Int =0) {
    //kt中的companion object 用于取代java中的static关键字
    //在companion object中包裹所有的静态属性和方法
    //可以用于更优雅的实现java中的单例
    companion object {
        val TYPE_REDPACK = 0
        val TYPE_COUNT = 1

        fun isRedpack(prize: Prize): Boolean {
            return prize.type == TYPE_REDPACK
        }
    }

    //使用object关键字来创建的单例
    object Singleton {
        var host: String = "127.0.0.1"
    }

}

data class dataClass(
    var name: String = "zhangsan",
    var street: String = "五家渠警校",
    var city: String = "上海",
    var state: String = "单身"
)

fun singletonTest() {
    Prize.Singleton.host
    Prize.isRedpack(prize = Prize())
    val address = dataClass(city = "北京")
    address.name = "李四"
}

//扩展方法
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val i = this[index1]
    this[index1] = this[index2]
    this[index2] = i
}

//扩展属性 他们的⾏为只能由显式提供的 getters/setters 定义。
val <T> List<T>.lastIndex: Int get() = size - 1