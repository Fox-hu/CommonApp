package com.fox.authandshare.share;

import android.app.Activity;

import com.fox.authandshare.platform.PlatForm;
import com.fox.authandshare.share.qq.QQSharer;
import com.fox.authandshare.share.sina.SinaShare;
import com.fox.authandshare.share.wechat.WeChatShare;


/**
 * Created by fox.hu on 2018/9/3.
 */

public class ShareFactory {

    public static IShare generateShare(Activity activity, PlatForm platForm) {
        IShare iShare = null;
        switch (platForm) {
            case QQ:
                iShare = new QQSharer(activity);
                break;
            case SINA:
                iShare = new SinaShare(activity);
                break;
            case WEIXIN:
                iShare = new WeChatShare(activity);
                break;
            default:
                break;
        }
        return iShare;
    }
}
