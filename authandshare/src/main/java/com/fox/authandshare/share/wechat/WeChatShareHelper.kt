package com.fox.authandshare.share.wechat

import android.os.Bundle

import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType


/**
 * Created by fox.hu on 2018/9/7.
 */

class WeChatShareHelper : ShareParamsHelper<WeChatShareBean> {

    override fun createParams(type: ShareType, weChatShareBean: WeChatShareBean): Bundle {
        val params = Bundle()
        params.putParcelable(WE_CHAT_BUNDLE, weChatShareBean)
        return params
    }

    companion object {
        val WE_CHAT_BUNDLE = "weixin_bundle"

        fun createText(text: String, desc: String): WeChatShareBean {
            val builder = WeChatShareBean.Builder()
            return builder.text(text).mediaDescr(desc).bulid()
        }

        fun createImg(imgPath: String): WeChatShareBean {
            val builder = WeChatShareBean.Builder()
            return builder.imgPath(imgPath).bulid()
        }

        fun createMp3(
            mp3Url: String, title: String, desc: String,
            thumbRes: Int
        ): WeChatShareBean {
            val builder = WeChatShareBean.Builder()
            return builder.mp3Url(mp3Url).mediaTitle(title).mediaDescr(desc).thumbImgId(thumbRes)
                .bulid()
        }

        fun createVideo(
            videoUrl: String, title: String, desc: String,
            thumbRes: Int
        ): WeChatShareBean {
            val builder = WeChatShareBean.Builder()
            return builder.videoUrl(videoUrl).mediaTitle(title).mediaDescr(desc)
                .thumbImgId(thumbRes)
                .bulid()
        }
    }
}
