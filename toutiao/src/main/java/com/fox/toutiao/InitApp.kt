package com.fox.toutiao

import android.app.Application
import android.content.Context
import com.fox.toutiao.modlue.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class InitApp : Application() {

    companion object {
        private val TAG: String = InitApp::class.java.simpleName
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        startKoin {
            androidContext(this@InitApp)
            modules(appModule)
        }

//        QbSdk.initX5Environment(applicationContext, object : QbSdk.PreInitCallback {
//            override fun onCoreInitFinished() {
//                "app onCoreInitFinished".logd()
//            }
//
//            override fun onViewInitFinished(p0: Boolean) {
//                "app onViewInitFinished is $p0".logd(TAG)
//            }
//        })
    }
}