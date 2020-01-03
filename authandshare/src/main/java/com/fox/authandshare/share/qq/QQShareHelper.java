package com.fox.authandshare.share.qq;

import android.os.Bundle;

import com.fox.authandshare.share.ShareParamsHelper;
import com.fox.authandshare.share.ShareType;
import com.tencent.connect.share.QQShare;


/**
 * Created by fox.hu on 2018/9/4.
 */

public class QQShareHelper implements ShareParamsHelper<QQShareBean> {
    private static final String TAG = QQShareHelper.class.getSimpleName();

    @Override
    public Bundle createParams(ShareType type, QQShareBean bean) {
        final Bundle params = new Bundle();
        int shareType;
        switch (type) {
            case TEXT:
                shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
                break;
            case IMAGE:
                shareType = QQShare.SHARE_TO_QQ_TYPE_IMAGE;
                break;
            case VIDEO:
                shareType = QQShare.SHARE_TO_QQ_TYPE_AUDIO;
                break;
            case APP:
                shareType = QQShare.SHARE_TO_QQ_TYPE_APP;
                break;
            default:
                shareType = QQShare.SHARE_TO_QQ_TYPE_DEFAULT;
                break;
        }
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, shareType);

        if (type != ShareType.IMAGE) {
            params.putString(QQShare.SHARE_TO_QQ_TITLE, bean.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, bean.getTargetUrl());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, bean.getSummary());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, bean.getImgUrl());
        } else {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, bean.getImgLocalUrl());
        }

        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, bean.getAppName());
        if (type == ShareType.VIDEO) {
            params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, bean.getAudioUrl());
        }

        return params;
    }

    public static QQShareBean createText(String title, String targetUrl, String imgUrl,
                                         String summary, String appName) {
        QQShareBean.Builder builder = new QQShareBean.Builder();
        return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl).appName(
                appName).build();
    }

    public static QQShareBean createImg(String imgLocalUrl) {
        QQShareBean.Builder builder = new QQShareBean.Builder();
        return builder.imgLocalUrl(imgLocalUrl).build();
    }

    public static QQShareBean createAudio(String title, String targetUrl, String imgUrl,
                                          String summary, String appName, String audioUrl) {
        QQShareBean.Builder builder = new QQShareBean.Builder();
        return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl).appName(
                appName).audioUrl(audioUrl).build();
    }

    public static QQShareBean createApp(String title, String targetUrl, String imgUrl,
                                        String summary) {
        QQShareBean.Builder builder = new QQShareBean.Builder();
        return builder.title(title).imgUrl(imgUrl).summary(summary).targetUrl(targetUrl).build();
    }
}
