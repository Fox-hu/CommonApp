package com.silver.fox.core.lifecycle

import android.app.Activity
import java.util.*

object KtxManager {
    private val mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() = if (mActivityList.isEmpty()) null else mActivityList.last

    fun pushActivity(activity: Activity) {
        //如果list中包含activity, 并且不是最后一项 则先删除该activity 再添加到最后一项
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    }

    fun popActivity(activity: Activity){
        mActivityList.remove(activity)
    }
}