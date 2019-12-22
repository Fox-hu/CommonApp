package com.silver.fox.common

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

open class InitApp : Application() {

    companion object {
        private val TAG: String = InitApp::class.java.simpleName
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }
}