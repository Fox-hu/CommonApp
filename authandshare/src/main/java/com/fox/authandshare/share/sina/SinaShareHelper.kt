package com.fox.authandshare.share.sina

import android.os.Bundle

import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType


/**
 * Created by fox.hu on 2018/9/4.
 */

class SinaShareHelper : ShareParamsHelper<SinaShareBean> {

    override fun createParams(type: ShareType, sinaShareBean: SinaShareBean): Bundle {
        val params = Bundle()
        params.putParcelable(SINA_BUNDLE, sinaShareBean)
        return params
    }

    companion object {
        val SINA_BUNDLE = "sina_bundle"

        fun createText(text: String, title: String, actionUrl: String): SinaShareBean {
            val builder = SinaShareBean.Builder()
            return builder.text(text).title(title).actionUrl(actionUrl).bulid()
        }

        fun createImg(resId: Int): SinaShareBean {
            val builder = SinaShareBean.Builder()
            return builder.resId(resId).bulid()
        }

        fun createVideo(audioPath: String): SinaShareBean {
            val builder = SinaShareBean.Builder()
            return builder.audioPath(audioPath).bulid()
        }

        fun createMultiple(
            text: String, title: String, actionUrl: String,
            resId: Int, audioPath: String
        ): SinaShareBean {
            val builder = SinaShareBean.Builder()
            return builder.text(text).title(title).actionUrl(actionUrl).resId(resId).audioPath(
                audioPath
            ).bulid()
        }
    }

}
