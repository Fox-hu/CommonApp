package com.component.kotlintest.first

class Prize(val name: String, val count: Int, val type: Int) {
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
    object singleton {
        var host:String = "127.0.0.1"
    }

}

fun singletonTest(){
    Prize.singleton.host
}