package com.fox.authandshare.share.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.fox.authandshare.platform.PlatForm
import com.fox.authandshare.share.IShare
import com.fox.authandshare.share.ShareListener
import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError

/**
 * Created by fox.hu on 2018/8/31.
 */

class QQSharer(activity: Activity) : IShare {
    private val mTencent: Tencent
    private var listener: ShareListener? = null

    override val paramsHelper: ShareParamsHelper<*>
        get() = QQShareHelper()

    private val qqShareListener = object : IUiListener {
        override fun onCancel() {
            listener!!.onCancel()
        }

        override fun onComplete(response: Any) {
            listener!!.onComplete(response.toString())
        }

        override fun onError(e: UiError) {
            listener!!.onError(e.errorMessage)
        }
    }

    init {
        mTencent = Tencent.createInstance(PlatForm.QQ.appId, activity.applicationContext)
    }

    override fun isInstall(context: Context): Boolean {
        return mTencent.isQQInstalled(context)
    }

    override fun shareTo(
        type: ShareType, activity: Activity, bundle: Bundle,
        listener: ShareListener
    ) {
        this.listener = listener
        mTencent.shareToQQ(activity, bundle, qqShareListener)
    }

    override fun onNewIntent(intent: Intent) {

    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener)
        }
    }

    companion object {
        private val TAG = QQSharer::class.java.simpleName
    }
}
