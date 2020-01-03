package com.fox.authandshare.share.qq

import android.os.Bundle

import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType
import com.tencent.connect.share.QQShare


/**
 * Created by fox.hu on 2018/9/4.
 */

class QQShareHelper : ShareParamsHelper<QQShareBean> {

    override fun createParams(type: ShareType, bean: QQShareBean): Bundle {
        val params = Bundle()
        val shareType: Int
        when (type) {
            ShareType.TEXT -> shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
            ShareType.IMAGE -> shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE
            ShareType.VIDEO -> shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO
            ShareType.APP -> shareType = QQShare.SHARE_TO_QQ_TYPE_APP
            else -> shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT
        }
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType)

        if (type != ShareType.IMAGE) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.title)
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.targetUrl)
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.summary)
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.imgUrl)
        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, bean.imgLocalUrl)
        }

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, bean.appName)
        if (type == ShareType.VIDEO) {
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, bean.audioUrl)
        }

        return params
    }

    companion object {
        private val TAG = QQShareHelper::class.java.simpleName

        fun createText(
            title: String, targetUrl: String, imgUrl: String,
            summary: String, appName: String
        ): QQShareBean {
            val builder = QQShareBean.Builder()
            return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl)
                .appName(
                    appName
                ).build()
        }

        fun createImg(imgLocalUrl: String): QQShareBean {
            val builder = QQShareBean.Builder()
            return builder.imgLocalUrl(imgLocalUrl).build()
        }

        fun createAudio(
            title: String, targetUrl: String, imgUrl: String,
            summary: String, appName: String, audioUrl: String
        ): QQShareBean {
            val builder = QQShareBean.Builder()
            return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl)
                .appName(
                    appName
                ).audioUrl(audioUrl).build()
        }

        fun createApp(
            title: String, targetUrl: String, imgUrl: String,
            summary: String
        ): QQShareBean {
            val builder = QQShareBean.Builder()
            return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl).build()
        }
    }
}
