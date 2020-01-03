package com.fox.authandshare.auth

import android.app.Activity
import android.content.Intent
import com.fox.authandshare.platform.PlatForm
import java.util.*

/**
 * @author fox.hu
 * @date 2018/8/16
 */

class AuthManager private constructor() {
    private val mAuthLoginMap = HashMap<PlatForm, IAuth>()
    private val mAuthListenerMap = HashMap<PlatForm, AuthListener>()
    private var mCurrentPlatForm: PlatForm? = null

    fun fetchPlatFormInfo(
        platForm: PlatForm, activity: Activity,
        listener: AuthListener
    ) {
        var iAuth = mAuthLoginMap[platForm]
        if (iAuth == null) {
            iAuth = AuthFactory.generate(activity, platForm)
            mAuthLoginMap[platForm] = iAuth
        }
        mCurrentPlatForm = platForm
        mAuthListenerMap[platForm] = listener
        listener.onStart(mCurrentPlatForm!!)
        iAuth!!.fetchPlatFormInfo(activity, listener)
    }

    fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {
        if (mCurrentPlatForm != null) {
            val iAuth = mAuthLoginMap[mCurrentPlatForm!!]
            iAuth?.onActivityResultData(requestCode, resultCode, data)
        }
    }

    fun getIAuthListener(platForm: PlatForm): AuthListener? {
        return mAuthListenerMap[platForm]
    }

    private object Holder {
        internal val INSTANCE = AuthManager()
    }

    companion object {
        private val TAG = AuthManager::class.java.simpleName

        val default: AuthManager
            get() = Holder.INSTANCE
    }
}
