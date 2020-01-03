package com.fox.authandshare.auth.sina

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.fox.authandshare.auth.AuthListener
import com.fox.authandshare.auth.HttpUtils
import com.fox.authandshare.auth.IAuth
import com.fox.authandshare.auth.PlatFormInfo
import com.fox.authandshare.platform.PlatForm
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.*
import com.sina.weibo.sdk.auth.sso.SsoHandler
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * @author fox.hu
 * @date 2018/8/24
 */

class SinaAuth(activity: Activity) : IAuth {
    private val mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAccessToken: Oauth2AccessToken? = null
    private val mSsoHandler: SsoHandler?
    private var mAuthListener: AuthListener? = null

    private val sinaAuthListener = object : WbAuthListener {
        override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
            mAccessToken = oauth2AccessToken
            if (mAccessToken!!.isSessionValid && mActivity.get() != null) {
                AccessTokenKeeper.writeAccessToken(mActivity.get(), mAccessToken)
                getAccessToken(mAccessToken!!.token)
            }
        }

        override fun cancel() {
            mAuthListener!!.onCancel()
        }

        override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
            mAuthListener!!.onError(wbConnectErrorMessage.errorMessage)
        }
    }

    internal var resultCallback: HttpUtils.ResultCallback<String> =
        object : HttpUtils.ResultCallback<String>() {
            override fun onSuccess(response: String) {
                var id: String? = null
                var screenName: String? = null
                var gender: String? = null
                var avatarLarge: String? = null
                try {
                    val jsonObject = JSONObject(response)
                    id = jsonObject.getString("id")
                    screenName = jsonObject.getString("screen_name")
                    gender = jsonObject.getString("gender")
                    avatarLarge = jsonObject.getString("avatar_large")

                    val sex = if (gender == "m") "男" else "女"
                    mAuthListener!!.onComplete(PlatFormInfo(id, screenName, sex, avatarLarge))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(e: Exception) {
                mAuthListener!!.onError(e.message!!)
            }
        }

    init {
        WbSdk.install(
            activity,
            AuthInfo(
                activity, PlatForm.SINA.appId, PlatForm.SINA.appKey,
                PlatForm.SINA.scope
            )
        )
        mSsoHandler = SsoHandler(activity)
    }

    override fun isInstall(context: Context): Boolean {
        return WbSdk.isWbInstall(context)
    }

    override fun fetchPlatFormInfo(activity: Activity, listener: AuthListener) {
        mAuthListener = listener
        mSsoHandler!!.authorize(sinaAuthListener)
    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {
        mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }


    private fun getAccessToken(access_token: String) {
        val loginUrl = StringBuffer()
        loginUrl.append("https://api.weibo.com/2/users/show.json").append("?access_token=").append(
            access_token
        ).append("&uid=").append(mAccessToken!!.uid)
        HttpUtils.get(loginUrl.toString(), resultCallback)
    }

    companion object {
        private val TAG = SinaAuth::class.java.simpleName
    }
}
