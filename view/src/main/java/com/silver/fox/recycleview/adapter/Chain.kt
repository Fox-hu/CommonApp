package com.silver.fox.recycleview.adapter

/**
 * @Author fox.hu
 * @Date 2020/5/14 14:13
 */
interface Chain<T> {
    fun indexItem(var1: Int, var2: T): Int
}

class DefaultChain<T> : Chain<T> {
    override fun indexItem(var1: Int, var2: T):Int = 0
}