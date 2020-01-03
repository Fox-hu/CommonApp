package com.fox.authandshare.auth.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.fox.authandshare.auth.AuthListener;
import com.fox.authandshare.auth.HttpUtils;
import com.fox.authandshare.auth.IAuth;
import com.fox.authandshare.auth.PlatFormInfo;
import com.fox.authandshare.platform.PlatForm;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * @author fox.hu
 * @date 2018/8/24
 */

public class SinaAuth implements IAuth {
    private static final String TAG = SinaAuth.class.getSimpleName();
    private WeakReference<Activity> mActivity;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private AuthListener mAuthListener;

    public SinaAuth(Activity activity) {
        mActivity = new WeakReference<>(activity);
        WbSdk.install(activity,
                new AuthInfo(activity, PlatForm.SINA.getAppId(), PlatForm.SINA.getAppKey(),
                        PlatForm.SINA.getScope()));
        mSsoHandler = new SsoHandler(activity);
    }

    @Override
    public boolean isInstall(Context context) {
        return WbSdk.isWbInstall(context);
    }

    @Override
    public void fetchPlatFormInfo(Activity activity, AuthListener listener) {
        mAuthListener = listener;
        mSsoHandler.authorize(sinaAuthListener);
    }

    @Override
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private WbAuthListener sinaAuthListener = new WbAuthListener() {
        @Override
        public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
            mAccessToken = oauth2AccessToken;
            if (mAccessToken.isSessionValid() && mActivity.get() != null) {
                AccessTokenKeeper.writeAccessToken(mActivity.get(), mAccessToken);
                getAccessToken(mAccessToken.getToken());
            }
        }

        @Override
        public void cancel() {
            mAuthListener.onCancel();
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            mAuthListener.onError(wbConnectErrorMessage.getErrorMessage());
        }
    };


    private void getAccessToken(String access_token) {
        StringBuffer loginUrl = new StringBuffer();
        loginUrl.append("https://api.weibo.com/2/users/show.json").append("?access_token=").append(
                access_token).append("&uid=").append(mAccessToken.getUid());
        HttpUtils.get(loginUrl.toString(), resultCallback);
    }

    HttpUtils.ResultCallback<String> resultCallback = new HttpUtils.ResultCallback<String>() {
        @Override
        public void onSuccess(String response) {
            String id = null;
            String screenName = null;
            String gender = null;
            String avatarLarge = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                id = jsonObject.getString("id");
                screenName = jsonObject.getString("screen_name");
                gender = jsonObject.getString("gender");
                avatarLarge = jsonObject.getString("avatar_large");

                String sex = gender.equals("m") ? "男" : "女";
                mAuthListener.onComplete(new PlatFormInfo(id, screenName, sex, avatarLarge));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Exception e) {
            mAuthListener.onError(e.getMessage());
        }
    };
}
