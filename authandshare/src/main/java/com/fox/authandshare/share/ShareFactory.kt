package com.fox.authandshare.share

import android.app.Activity

import com.fox.authandshare.platform.PlatForm
import com.fox.authandshare.share.qq.QQSharer
import com.fox.authandshare.share.sina.SinaShare
import com.fox.authandshare.share.wechat.WeChatShare


/**
 * Created by fox.hu on 2018/9/3.
 */

object ShareFactory {

    fun generateShare(activity: Activity, platForm: PlatForm): IShare {

        return when (platForm) {
            PlatForm.QQ -> QQSharer(activity)
            PlatForm.SINA -> SinaShare(activity)
            PlatForm.WEIXIN -> WeChatShare(activity)
            else -> {
                QQSharer(activity)
            }
        }
    }
}
