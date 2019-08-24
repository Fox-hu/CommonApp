package com.component.kotlintest

import android.app.Application
import com.component.kotlintest.extensions.DelegatesExt

class App :Application(){
    //伴生对象，相当于static
    companion object{
        //属性委托 将属性委托给一个对象
        var instance:App by DelegatesExt.notNullSingleValue()
        // var instance:App by NotNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}