package com.fox.authandshare.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.fox.authandshare.platform.PlatForm
import java.util.*


/**
 * Created by fox.hu on 2018/9/3.
 */

class ShareManager private constructor() {
    private val mShareMap = HashMap<PlatForm, IShare>()
    private val mShareListenerMap = HashMap<PlatForm, ShareListener>()
    private var mCurrentPlatForm: PlatForm? = null

    fun shareTo(
        platForm: PlatForm, type: ShareType, activity: Activity, bundle: Bundle,
        listener: ShareListener
    ) {
        val iShare = getShare(platForm, activity)
        mCurrentPlatForm = platForm
        mShareListenerMap[platForm] = listener
        listener.onStart(platForm)
        iShare!!.shareTo(type, activity, bundle, listener)
    }

    fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {
        if (mCurrentPlatForm != null) {
            val iShare = mShareMap[mCurrentPlatForm!!]
            iShare?.onActivityResultData(requestCode, resultCode, data)
        }
    }

    fun onNewIntent(intent: Intent) {
        if (mCurrentPlatForm != null) {
            val iShare = mShareMap[mCurrentPlatForm!!]
            iShare?.onNewIntent(intent)
        }
    }

    fun getParamsHelper(activity: Activity, platForm: PlatForm): ShareParamsHelper<*> {
        val iShare = getShare(platForm, activity)
        return iShare!!.paramsHelper
    }

    fun getIShareListener(platForm: PlatForm): ShareListener? {
        return mShareListenerMap[platForm]
    }

    private fun getShare(platForm: PlatForm, activity: Activity): IShare? {
        var iShare = mShareMap[platForm]
        if (iShare == null) {
            iShare = ShareFactory.generateShare(activity, platForm)
            mShareMap[platForm] = iShare
        }
        return iShare
    }

    private object Holder {
        internal val INSTANCE = ShareManager()
    }

    companion object {
        private val TAG = ShareManager::class.java.simpleName

        val default: ShareManager
            get() = Holder.INSTANCE
    }
}
