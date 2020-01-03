package com.fox.authandshare.auth


import com.fox.authandshare.platform.PlatForm

/**
 * 第三方登录的通用回调
 * @author fox.hu
 * @date 2018/8/16
 */

interface AuthListener {

    /**
     * @param platform
     */
    fun onStart(platform: PlatForm)

    /**
     * @param platformInfo
     */
    fun onComplete(platformInfo: PlatFormInfo)

    /**
     * @param errorMsg
     */
    fun onError(errorMsg: String)

    /**
     *
     */
    fun onCancel()
}
