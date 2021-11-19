package com.fox.authandshare.auth

import android.app.Activity

import com.fox.authandshare.auth.qq.QQAuth
import com.fox.authandshare.auth.wechat.WeChatAuth
import com.fox.authandshare.platform.PlatForm


/**
 * @author fox.hu
 * @date 2018/8/16
 */

object AuthFactory {

    fun generate(activity: Activity, platForm: PlatForm): IAuth {
        return when (platForm) {
            PlatForm.QQ -> QQAuth(activity)
            PlatForm.WEIXIN -> WeChatAuth(activity)
//            PlatForm.SINA -> SinaAuth(activity)
            else -> {
                QQAuth(activity)
            }
        }
    }
}
