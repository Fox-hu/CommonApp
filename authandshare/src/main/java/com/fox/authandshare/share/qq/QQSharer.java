package com.fox.authandshare.share.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fox.authandshare.platform.PlatForm;
import com.fox.authandshare.share.IShare;
import com.fox.authandshare.share.ShareListener;
import com.fox.authandshare.share.ShareParamsHelper;
import com.fox.authandshare.share.ShareType;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by fox.hu on 2018/8/31.
 */

public class QQSharer implements IShare {
    private static final String TAG = QQSharer.class.getSimpleName();
    private Tencent mTencent;
    private ShareListener listener;

    public QQSharer(Activity activity) {
        mTencent = Tencent.createInstance(PlatForm.QQ.getAppId(), activity.getApplicationContext());
    }

    @Override
    public boolean isInstall(Context context) {
        return mTencent.isQQInstalled(context);
    }

    @Override
    public ShareParamsHelper getParamsHelper() {
        return new QQShareHelper();
    }

    @Override
    public void shareTo(ShareType type, Activity activity, Bundle bundle,
                       ShareListener listener) {
        this.listener = listener;
        mTencent.shareToQQ(activity, bundle, qqShareListener);
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_QQ_SHARE) {
            Tencent.onActivityResultData(requestCode, resultCode, data, qqShareListener);
        }
    }

    private IUiListener qqShareListener = new IUiListener() {
        @Override
        public void onCancel() {
            listener.onCancel();
        }

        @Override
        public void onComplete(Object response) {
            listener.onComplete(response.toString());
        }

        @Override
        public void onError(UiError e) {
            listener.onError(e.errorMessage);
        }
    };
}
