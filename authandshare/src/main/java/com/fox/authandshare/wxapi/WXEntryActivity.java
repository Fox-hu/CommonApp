package com.fox.authandshare.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.fox.authandshare.auth.AuthListener;
import com.fox.authandshare.auth.AuthManager;
import com.fox.authandshare.auth.HttpUtils;
import com.fox.authandshare.auth.PlatFormInfo;
import com.fox.authandshare.platform.PlatForm;
import com.fox.authandshare.share.ShareListener;
import com.fox.authandshare.share.ShareManager;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fox.hu on 2018/8/17.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXEntryActivity.class.getSimpleName();
    private AuthListener authListener;
    private ShareListener shareListener;
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PlatForm.WEIXIN.getAppId());
        authListener = AuthManager.getDefault().getIAuthListener(PlatForm.WEIXIN);
        shareListener = ShareManager.getDefault().getIShareListener(PlatForm.WEIXIN);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        //登录回调 分享回调和授权回调都在此处理，根据前面的baseResp.transaction来判断是分享还是授权
        //如果选择留在微信 则无法收到回调信息
        if (!TextUtils.isEmpty(baseResp.transaction)) {
            handleShareCallBack(baseResp);
        } else {
            handleAuthCallBack(baseResp);
        }
        finish();
    }

    private void handleAuthCallBack(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) baseResp).code;
                getAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (authListener != null) {
                    authListener.onError("用户拒绝授权");
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (authListener != null) {
                    authListener.onCancel();
                }
                break;
            default:
                if (authListener != null) {
                    authListener.onError("授权失败");
                }
                break;
        }
    }

    private void handleShareCallBack(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (shareListener != null) {
                    shareListener.onComplete("分享成功!");
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if (shareListener != null) {
                    shareListener.onError("分享被拒绝");
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (shareListener != null) {
                    shareListener.onCancel();
                }
                break;
            default:
                if (shareListener != null) {
                    shareListener.onError("分享失败");
                }
                break;
        }
    }


    private void getAccessToken(String code) {
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weixin.qq.com/sns/oauth2/access_token").append("?appid=")
                .append(PlatForm.WEIXIN.getAppId()).append("&secret=").append(
                PlatForm.WEIXIN.getAppKey()).append("&code=").append(code).append(
                "&grant_type=authorization_code");
        HttpUtils.get(loginUrl.toString(), resultCallback);
    }

    HttpUtils.ResultCallback<String> resultCallback = new HttpUtils.ResultCallback<String>() {
        @Override
        public void onSuccess(String response) {
            String access = null;
            String openId = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                access = jsonObject.getString("access_token");
                openId = jsonObject.getString("openid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //获取个人信息
            String getUserInfo =
                    "https://api.weixin.qq.com/sns/userinfo?access_token=" + access + "&openid=" +
                    openId;
            HttpUtils.get(getUserInfo, reCallback);
        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            finish();
            authListener.onError("授权失败");
        }
    };

    HttpUtils.ResultCallback<String> reCallback = new HttpUtils.ResultCallback<String>() {
        @Override
        public void onSuccess(String responses) {

            String nickName = null;
            String sex = null;
            String city = null;
            String province = null;
            String country = null;
            String headimgurl = null;
            String openId = null;
            String unionid = null;
            try {
                JSONObject jsonObject = new JSONObject(responses);
                openId = jsonObject.getString("openid");
                nickName = jsonObject.getString("nickname");
                sex = jsonObject.getString("sex");
                city = jsonObject.getString("city");
                province = jsonObject.getString("province");
                country = jsonObject.getString("country");
                headimgurl = jsonObject.getString("headimgurl");
                unionid = jsonObject.getString("unionid");

                String gender = sex.equals("1") ? "男" : "女";
                authListener.onComplete(new PlatFormInfo(openId, nickName, gender, headimgurl));
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(WXEntryActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            finish();
            authListener.onError("授权失败");
        }
    };
}
