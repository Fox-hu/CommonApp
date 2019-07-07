package com.component.kotlintest

import android.app.Application
import com.component.kotlintest.extensions.DelegatesExt

class App :Application(){
    companion object{
        var instance:App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}