package com.fox.authandshare.auth.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log

import com.fox.authandshare.auth.AuthListener
import com.fox.authandshare.auth.IAuth
import com.fox.authandshare.auth.PlatFormInfo
import com.fox.authandshare.platform.PlatForm
import com.tencent.connect.UserInfo
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

import org.json.JSONException
import org.json.JSONObject

import java.lang.ref.WeakReference

/**
 * @author fox.hu
 * @date 2018/8/17
 */

class QQAuth(activity: Activity) : IAuth {

    private val mTencent: Tencent?
    private val mActivity: WeakReference<Activity>
    private var authListener: AuthListener? = null
    private var mInfo: UserInfo? = null

    internal var loginListener: IUiListener = object : BaseUiListener() {
        override fun doComplete(values: JSONObject) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime())
            initOpenidAndToken(values)
            updateUserInfo()
        }
    }

    init {
        mActivity = WeakReference(activity)
        mTencent = Tencent.createInstance(PlatForm.QQ.appId, activity.applicationContext)
    }

    override fun isInstall(context: Context): Boolean {
        return mTencent!!.isQQInstalled(context)
    }

    override fun fetchPlatFormInfo(activity: Activity, listener: AuthListener) {
        authListener = listener
        mTencent!!.login(activity, "all", loginListener)
    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener)
        }
    }

    private open inner class BaseUiListener : IUiListener {

        override fun onComplete(response: Any?) {
            if (null == response) {
                return
            }
            val jsonResponse = response as JSONObject?
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return
            }
            doComplete(response)
        }

        protected open fun doComplete(values: JSONObject) {

        }

        override fun onError(e: UiError) {
            Log.e(TAG, "onLogin onError E = " + e.errorMessage)
            authListener!!.onError(e.errorMessage)
        }

        override fun onCancel() {
            Log.e(TAG, "onLogin onCancel")
            authListener!!.onCancel()
        }
    }

    private fun initOpenidAndToken(jsonObject: JSONObject) {
        try {
            val token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN)
            val expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN)
            val openId = jsonObject.getString(Constants.PARAM_OPEN_ID)
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(
                    openId
                )
            ) {
                mTencent!!.setAccessToken(token, expires)
                mTencent.openId = openId
            }
        } catch (e: Exception) {
            authListener!!.onError(e.message!!)
        }

    }

    private fun updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid && mActivity.get() != null) {
            val listener = object : IUiListener {

                override fun onError(e: UiError) {
                    Log.e(TAG, "updateUserInfo onError E = " + e.errorMessage)
                    authListener!!.onError(e.errorMessage)
                }

                override fun onComplete(response: Any) {
                    Log.e(TAG, "updateUserInfo response = $response")
                    authListener!!.onComplete(response2PlatFormInfo(response as JSONObject))
                }

                override fun onCancel() {
                    Log.e(TAG, "updateUserInfo onCancel")
                    authListener!!.onCancel()
                }
            }
            mInfo = UserInfo(mActivity.get(), mTencent.qqToken)
            mInfo!!.getUserInfo(listener)
        }
    }

    private fun response2PlatFormInfo(response: JSONObject): PlatFormInfo {
        try {
            val nickName = response.getString(Constants.PARAM_NICK_NAME)
            val gender = response.getString(Constants.PARAM_GENDER)
            val iconUrl = response.getString(Constants.PARAM_ICON_URL)
            val openId = mTencent!!.openId

            return PlatFormInfo(openId, nickName, gender, iconUrl)
        } catch (e: JSONException) {
            authListener!!.onError(e.message!!)
        }

        return PlatFormInfo("", "", "", "")
    }

    companion object {
        private val TAG = QQAuth::class.java.simpleName
    }
}
