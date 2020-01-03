package com.fox.authandshare.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by fox.hu on 2018/8/31.
 */

public interface IShare {

    boolean isInstall(Context context);

    ShareParamsHelper getParamsHelper();

    void shareTo(ShareType type, Activity activity, Bundle bundle, ShareListener listener);

    /**
     * 在activity onNewIntent 新浪需要
     * @param intent
     */
    void onNewIntent(Intent intent);

    /**
     * 在activity 的onActivityResult使用 QQ需要
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    void onActivityResultData(int requestCode, int resultCode, Intent data);
}
