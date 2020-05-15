package com.silver.fox.recycleview.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * @Author fox.hu
 * @Date 2020/5/14 14:24
 */
class ViewTypes {
    private val classes: MutableList<Class<*>> = ArrayList()
    private val vHolders: MutableList<VHolder<*, *>> = ArrayList()
    private val chains: MutableList<Chain<*>> = ArrayList()

    fun <T, VH:RecyclerView.ViewHolder> save(clazz: Class<T>, vHolder: VHolder<T, VH>, chain: Chain<T> = DefaultChain<T>()) {
        classes.add(clazz)
        vHolders.add(vHolder)
        chains.add(chain)
    }

    val size: Int
        get() = classes.size

    fun getClassIndexOf(clazz: Class<*>): Int = classes.indexOf(clazz)

    fun getItemView(index: Int): VHolder<*,*> = vHolders[index]

    fun getChain(index: Int): Chain<*> = chains[index]
}