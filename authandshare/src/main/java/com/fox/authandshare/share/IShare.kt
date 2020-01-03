package com.fox.authandshare.share

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Created by fox.hu on 2018/8/31.
 */

interface IShare {

    val paramsHelper: ShareParamsHelper<*>

    fun isInstall(context: Context): Boolean

    fun shareTo(type: ShareType, activity: Activity, bundle: Bundle, listener: ShareListener)

    /**
     * 在activity onNewIntent 新浪需要
     * @param intent
     */
    fun onNewIntent(intent: Intent)

    /**
     * 在activity 的onActivityResult使用 QQ需要
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent)
}
