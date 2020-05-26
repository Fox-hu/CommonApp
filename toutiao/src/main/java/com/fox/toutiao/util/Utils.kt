package com.fox.toutiao.util

import android.app.Activity
import android.view.View

/**
 * @Author fox
 * @Date 2020/5/26 21:50
 */

class Utils {

    companion object {
        @JvmStatic
        fun <T : View> findViewById(activity: Activity, viewId: Int): T {
            return activity.findViewById(viewId)
        }
    }
}