package com.fox.authandshare.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fox.authandshare.platform.PlatForm;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by fox.hu on 2018/9/3.
 */

public class ShareManager {
    private static final String TAG = ShareManager.class.getSimpleName();
    private Map<PlatForm, IShare> mShareMap = new HashMap<>();
    private Map<PlatForm, ShareListener> mShareListenerMap = new HashMap<>();
    private PlatForm mCurrentPlatForm;

    private ShareManager() {}

    public static ShareManager getDefault() {
        return Holder.INSTANCE;
    }

    public void shareTo(PlatForm platForm, ShareType type, Activity activity, Bundle bundle,
                        ShareListener listener) {
        IShare iShare = getShare(platForm, activity);
        mCurrentPlatForm = platForm;
        mShareListenerMap.put(platForm, listener);
        listener.onStart(platForm);
        iShare.shareTo(type, activity, bundle, listener);
    }

    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
        if (mCurrentPlatForm != null) {
            IShare iShare = mShareMap.get(mCurrentPlatForm);
            if (iShare != null) {
                iShare.onActivityResultData(requestCode, resultCode, data);
            }
        }
    }

    public void onNewIntent(Intent intent) {
        if (mCurrentPlatForm != null) {
            IShare iShare = mShareMap.get(mCurrentPlatForm);
            if (iShare != null) {
                iShare.onNewIntent(intent);
            }
        }
    }

    public ShareParamsHelper getParamsHelper(Activity activity, PlatForm platForm) {
        IShare iShare = getShare(platForm, activity);
        return iShare.getParamsHelper();
    }

    public ShareListener getIShareListener(PlatForm platForm) {
        return mShareListenerMap.get(platForm);
    }

    private IShare getShare(PlatForm platForm, Activity activity) {
        IShare iShare = mShareMap.get(platForm);
        if (iShare == null) {
            iShare = ShareFactory.generateShare(activity, platForm);
            mShareMap.put(platForm, iShare);
        }
        return iShare;
    }

    private static class Holder {
        static final ShareManager INSTANCE = new ShareManager();
    }
}
