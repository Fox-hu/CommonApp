package com.fox.toutiao



import com.fox.toutiao.modlue.appModule
import com.silver.fox.common.InitApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class ToutiaoApp : InitApp() {

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        startKoin {
            androidContext(this@ToutiaoApp)
            modules(appModule)
        }
    }
}