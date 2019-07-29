package com.component.kotlintest

import android.app.Application
import com.component.kotlintest.extensions.DelegatesExt

class App :Application(){
    //属性委托 将属性委托给一个对象
    companion object{
        var instance:App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}