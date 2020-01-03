package com.fox.authandshare.auth;

import android.app.Activity;

import com.fox.authandshare.auth.qq.QQAuth;
import com.fox.authandshare.auth.sina.SinaAuth;
import com.fox.authandshare.auth.wechat.WeChatAuth;
import com.fox.authandshare.platform.PlatForm;


/**
 * @author fox.hu
 * @date 2018/8/16
 */

public class AuthFactory {

    public static IAuth generate(Activity activity, PlatForm platForm) {
        IAuth iAuth = null;
        switch (platForm) {
            case QQ:
                iAuth = new QQAuth(activity);
                break;
            case WEIXIN:
                iAuth = new WeChatAuth(activity);
                break;
            case SINA:
                iAuth = new SinaAuth(activity);
                break;
            default:
                break;
        }
        return iAuth;
    }
}
