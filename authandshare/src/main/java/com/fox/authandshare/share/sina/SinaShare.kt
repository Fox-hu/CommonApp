package com.fox.authandshare.share.sina

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.fox.authandshare.platform.PlatForm
import com.fox.authandshare.share.IShare
import com.fox.authandshare.share.ShareListener
import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.api.ImageObject
import com.sina.weibo.sdk.api.TextObject
import com.sina.weibo.sdk.api.VideoSourceObject
import com.sina.weibo.sdk.api.WeiboMultiMessage
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.share.WbShareCallback
import com.sina.weibo.sdk.share.WbShareHandler
import java.io.File

/**
 * Created by fox.hu on 2018/9/3.
 */

class SinaShare(activity: Activity) : IShare, WbShareCallback {
    private val shareHandler: WbShareHandler
    private var listener: ShareListener? = null
    private val context: Context

    override val paramsHelper: ShareParamsHelper<*>
        get() = SinaShareHelper()

    init {
        context = activity.applicationContext
        WbSdk.install(
            activity,
            AuthInfo(
                activity, PlatForm.SINA.appId, PlatForm.SINA.appKey,
                PlatForm.SINA.scope
            )
        )
        shareHandler = WbShareHandler(activity)
        shareHandler.registerApp()
    }

    override fun isInstall(context: Context): Boolean {
        return WbSdk.isWbInstall(context)
    }

    override fun shareTo(
        type: ShareType,
        activity: Activity,
        bundle: Bundle,
        listener: ShareListener
    ) {
        this.listener = listener
        val weiboMessage = WeiboMultiMessage()
        val bean = bundle.getParcelable<SinaShareBean>(SinaShareHelper.SINA_BUNDLE)
        when (type) {
            ShareType.TEXT -> weiboMessage.textObject = getTextObj(bean!!)
            ShareType.IMAGE -> weiboMessage.imageObject = getImageObj(bean!!)
            ShareType.VIDEO -> weiboMessage.videoSourceObject = getVideoObject(bean!!)
            ShareType.APP -> {
                //todo 此处为了测试 之后删掉
                weiboMessage.textObject = getTextObj(bean!!)
                weiboMessage.imageObject = getImageObj(bean)
                weiboMessage.videoSourceObject = getVideoObject(bean)
            }
            ShareType.MULTIPLE -> {
                weiboMessage.textObject = getTextObj(bean!!)
                weiboMessage.imageObject = getImageObj(bean)
                weiboMessage.videoSourceObject = getVideoObject(bean)
            }
            else -> {
            }
        }
        shareHandler.shareMessage(weiboMessage, false)
    }

    private fun getVideoObject(bean: SinaShareBean): VideoSourceObject? {
        val filePath = bean.audioPath
        if (TextUtils.isEmpty(filePath)) {
            return null
        } else {
            val videoSourceObject = VideoSourceObject()
            videoSourceObject.videoPath = Uri.fromFile(File(filePath!!))
            return videoSourceObject
        }
    }

    private fun getImageObj(bean: SinaShareBean): ImageObject? {
        val imgUrl = bean.resId
        if (imgUrl == 0) {
            return null
        } else {
            val imageObject = ImageObject()
            val bitmap = BitmapFactory.decodeResource(context.resources, imgUrl)
            imageObject.setImageObject(bitmap)
            return imageObject
        }
    }

    private fun getTextObj(bean: SinaShareBean): TextObject {
        val textObject = TextObject()
        textObject.text = bean.text
        textObject.title = bean.title
        textObject.actionUrl = bean.actionUrl
        return textObject
    }

    override fun onNewIntent(intent: Intent) {
        shareHandler.doResultIntent(intent, this)
    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {}

    override fun onWbShareSuccess() {
        listener!!.onComplete("share success!")
    }

    override fun onWbShareCancel() {
        listener!!.onCancel()
    }

    override fun onWbShareFail() {
        listener!!.onError("share failed")
    }

    companion object {
        private val TAG = SinaShare::class.java.simpleName
    }
}
