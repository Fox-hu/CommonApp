package com.fox.authandshare.auth

import android.app.Activity
import android.content.Context
import android.content.Intent

/**
 *
 * 第三方登录的抽象接口 所有第三方登录都需要实现该接口
 * @author fox.hu
 * @date 2018/8/16
 */

interface IAuth {
    /**
     * 第三方登录平台是否已经安装
     * @param context
     * @return 第三方登录平台是否已经安装
     */
    fun isInstall(context: Context): Boolean

    /**
     * 获取第三方登录平台的用户信息
     * @param activity
     * @param listener 通用回调方法，不同的平台使用统一的回调，在此处统一
     */
    fun fetchPlatFormInfo(activity: Activity, listener: AuthListener)

    /**
     * 在activity 的onActivityResult使用 QQ和新浪需要
     * @param requestCode
     * @param resultCode
     * @param data
     */
    fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent)

}
