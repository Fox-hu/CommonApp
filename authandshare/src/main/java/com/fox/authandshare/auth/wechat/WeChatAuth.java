package com.fox.authandshare.auth.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.fox.authandshare.auth.AuthListener;
import com.fox.authandshare.auth.IAuth;
import com.fox.authandshare.platform.PlatForm;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author fox.hu
 * @date 2018/8/17
 */

public class WeChatAuth implements IAuth {
    private IWXAPI api;

    public WeChatAuth(Activity activity) {
        api = WXAPIFactory.createWXAPI(activity.getApplicationContext(),
                PlatForm.WEIXIN.getAppId());
        api.registerApp(PlatForm.WEIXIN.getAppId());
    }

    @Override
    public boolean isInstall(Context context) {
        return api.isWXAppInstalled();
    }

    @Override
    public void fetchPlatFormInfo(Activity activity, AuthListener listener) {
        if (api != null && !api.isWXAppInstalled()) {
            listener.onError("您还未安装微信客户端");
            return;
        }

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_微信登录";

        api.sendReq(req);
    }

    @Override
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {

    }
}
