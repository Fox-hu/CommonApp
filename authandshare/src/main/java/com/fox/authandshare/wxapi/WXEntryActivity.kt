package com.fox.authandshare.wxapi

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast

import com.fox.authandshare.auth.AuthListener
import com.fox.authandshare.auth.AuthManager
import com.fox.authandshare.auth.HttpUtils
import com.fox.authandshare.auth.PlatFormInfo
import com.fox.authandshare.platform.PlatForm
import com.fox.authandshare.share.ShareListener
import com.fox.authandshare.share.ShareManager
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by fox.hu on 2018/8/17.
 */

class WXEntryActivity : Activity(), IWXAPIEventHandler {
    private var authListener: AuthListener? = null
    private var shareListener: ShareListener? = null
    private var api: IWXAPI? = null

    internal var resultCallback: HttpUtils.ResultCallback<String> =
        object : HttpUtils.ResultCallback<String>() {
            override fun onSuccess(response: String) {
                var access: String? = null
                var openId: String? = null
                try {
                    val jsonObject = JSONObject(response)
                    access = jsonObject.getString("access_token")
                    openId = jsonObject.getString("openid")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                //获取个人信息
                val getUserInfo =
                    "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" +
                            openId
                HttpUtils[getUserInfo, reCallback]
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@WXEntryActivity, "登录失败", Toast.LENGTH_SHORT).show()
                finish()
                authListener!!.onError("授权失败")
            }
        }

    internal var reCallback: HttpUtils.ResultCallback<String> =
        object : HttpUtils.ResultCallback<String>() {
            override fun onSuccess(responses: String) {

                var nickName: String? = null
                var sex: String? = null
                var city: String? = null
                var province: String? = null
                var country: String? = null
                var headimgurl: String? = null
                var openId: String? = null
                var unionid: String? = null
                try {
                    val jsonObject = JSONObject(responses)
                    openId = jsonObject.getString("openid")
                    nickName = jsonObject.getString("nickname")
                    sex = jsonObject.getString("sex")
                    city = jsonObject.getString("city")
                    province = jsonObject.getString("province")
                    country = jsonObject.getString("country")
                    headimgurl = jsonObject.getString("headimgurl")
                    unionid = jsonObject.getString("unionid")

                    val gender = if (sex == "1") "男" else "女"
                    authListener!!.onComplete(
                        PlatFormInfo(
                            openId!!,
                            nickName!!,
                            gender,
                            headimgurl!!
                        )
                    )
                    finish()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(e: Exception) {
                Toast.makeText(this@WXEntryActivity, "登录失败", Toast.LENGTH_SHORT).show()
                finish()
                authListener!!.onError("授权失败")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, PlatForm.WEIXIN.appId)
        authListener = AuthManager.default.getIAuthListener(PlatForm.WEIXIN)
        shareListener = ShareManager.default.getIShareListener(PlatForm.WEIXIN)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {

    }

    override fun onResp(baseResp: BaseResp) {
        //登录回调 分享回调和授权回调都在此处理，根据前面的baseResp.transaction来判断是分享还是授权
        //如果选择留在微信 则无法收到回调信息
        if (!TextUtils.isEmpty(baseResp.transaction)) {
            handleShareCallBack(baseResp)
        } else {
            handleAuthCallBack(baseResp)
        }
        finish()
    }

    private fun handleAuthCallBack(baseResp: BaseResp) {
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (baseResp as SendAuth.Resp).code
                getAccessToken(code)
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> if (authListener != null) {
                authListener!!.onError("用户拒绝授权")
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> if (authListener != null) {
                authListener!!.onCancel()
            }
            else -> if (authListener != null) {
                authListener!!.onError("授权失败")
            }
        }
    }

    private fun handleShareCallBack(baseResp: BaseResp) {
        when (baseResp.errCode) {
            BaseResp.ErrCode.ERR_OK -> if (shareListener != null) {
                shareListener!!.onComplete("分享成功!")
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> if (shareListener != null) {
                shareListener!!.onError("分享被拒绝")
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> if (shareListener != null) {
                shareListener!!.onCancel()
            }
            else -> if (shareListener != null) {
                shareListener!!.onError("分享失败")
            }
        }
    }


    private fun getAccessToken(code: String) {
        val loginUrl = StringBuffer()
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token").append("?appid=")
            .append(PlatForm.WEIXIN.appId).append("&secret=").append(
                PlatForm.WEIXIN.appKey
            ).append("&code=").append(code).append(
                "&grant_type=authorization_code"
            )
        HttpUtils[loginUrl.toString(), resultCallback]
    }

    companion object {
        private val TAG = WXEntryActivity::class.java.simpleName
    }
}
