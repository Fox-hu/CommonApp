package com.fox.authandshare.auth.wechat

import android.app.Activity
import android.content.Context
import android.content.Intent

import com.fox.authandshare.auth.AuthListener
import com.fox.authandshare.auth.IAuth
import com.fox.authandshare.platform.PlatForm
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * @author fox.hu
 * @date 2018/8/17
 */

class WeChatAuth(activity: Activity) : IAuth {
    private val api: IWXAPI?

    init {
        api = WXAPIFactory.createWXAPI(
            activity.applicationContext,
            PlatForm.WEIXIN.appId
        )
        api!!.registerApp(PlatForm.WEIXIN.appId)
    }

    override fun isInstall(context: Context): Boolean {
        return api!!.isWXAppInstalled
    }

    override fun fetchPlatFormInfo(activity: Activity, listener: AuthListener) {
        if (api != null && !api.isWXAppInstalled) {
            listener.onError("您还未安装微信客户端")
            return
        }

        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "wechat_sdk_微信登录"

        api!!.sendReq(req)
    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {

    }
}
