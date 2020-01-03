package com.fox.authandshare.share.wechat;

import android.os.Bundle;

import com.fox.authandshare.share.ShareParamsHelper;
import com.fox.authandshare.share.ShareType;


/**
 * Created by fox.hu on 2018/9/7.
 */

public class WeChatShareHelper implements ShareParamsHelper<WeChatShareBean> {
    public static final String WE_CHAT_BUNDLE = "weixin_bundle";

    @Override
    public Bundle createParams(ShareType type, WeChatShareBean weChatShareBean) {
        final Bundle params = new Bundle();
        params.putParcelable(WE_CHAT_BUNDLE, weChatShareBean);
        return params;
    }

    public static WeChatShareBean createText(String text, String desc) {
        WeChatShareBean.Builder builder = new WeChatShareBean.Builder();
        return builder.text(text).mediaDescr(desc).bulid();
    }

    public static WeChatShareBean createImg(String imgPath) {
       WeChatShareBean.Builder builder = new WeChatShareBean.Builder();
        return builder.imgPath(imgPath).bulid();
    }

    public static WeChatShareBean createMp3(String mp3Url, String title, String desc,
                                                                              int thumbRes) {
        WeChatShareBean.Builder builder = new WeChatShareBean.Builder();
        return builder.mp3Url(mp3Url).mediaTitle(title).mediaDescr(desc).thumbImgId(thumbRes)
                .bulid();
    }

    public static WeChatShareBean createVideo(String videoUrl, String title, String desc,
                                                                                int thumbRes) {
        WeChatShareBean.Builder builder = new WeChatShareBean.Builder();
        return builder.videoUrl(videoUrl).mediaTitle(title).mediaDescr(desc).thumbImgId(thumbRes)
                .bulid();
    }
}
