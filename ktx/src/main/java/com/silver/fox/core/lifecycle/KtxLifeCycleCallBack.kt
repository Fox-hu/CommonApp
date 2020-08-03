package com.silver.fox.core.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.silver.fox.ext.loge
import com.silver.fox.ext.logi

class KtxLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        KtxManager.pushActivity(activity)
        "onActivityCreated : ${activity.localClassName}".logi("KtxLifeCycleCallBack")
    }

    override fun onActivityStarted(activity: Activity) {
        "onActivityStarted:${activity.localClassName}".logi("KtxLifeCycleCallBack")
    }

    override fun onActivityResumed(activity: Activity) {
        "onActivityResumed : ${activity.localClassName}".logi("KtxLifeCycleCallBack")

    }

    override fun onActivityPaused(activity: Activity) {
        "onActivityPaused : ${activity.localClassName}".logi("KtxLifeCycleCallBack")
    }

    override fun onActivityDestroyed(activity: Activity) {
        "onActivityDestroyed : ${activity.localClassName}".logi("KtxLifeCycleCallBack")
        KtxManager.popActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
        "onActivitySaveInstanceState : ${activity.localClassName}".logi("KtxLifeCycleCallBack")
    }

    override fun onActivityStopped(activity: Activity) {
        "onActivityStopped : ${activity.localClassName}".logi("KtxLifeCycleCallBack")
    }
}