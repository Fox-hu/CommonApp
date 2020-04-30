package com.silver.fox.stateslayout

/**
 * created by francis.fan on 2019/12/2
 */

interface StateLayoutCallback {
    fun onReloadClick()
    fun onButtonClick()
}

abstract class StateLayoutViewModel : StateLayoutCallback {
    abstract override fun onReloadClick()
    override fun onButtonClick() {}
}

