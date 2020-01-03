package com.fox.authandshare.share.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.fox.authandshare.platform.PlatForm;
import com.fox.authandshare.share.IShare;
import com.fox.authandshare.share.ShareListener;
import com.fox.authandshare.share.ShareParamsHelper;
import com.fox.authandshare.share.ShareType;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;

/**
 * Created by fox.hu on 2018/9/3.
 */

public class SinaShare implements IShare, WbShareCallback {
    private static final String TAG = SinaShare.class.getSimpleName();
    private WbShareHandler shareHandler;
    private ShareListener listener;
    private Context context;

    public SinaShare(Activity activity) {
        context = activity.getApplicationContext();
        WbSdk.install(activity,
                new AuthInfo(activity, PlatForm.SINA.getAppId(), PlatForm.SINA.getAppKey(),
                        PlatForm.SINA.getScope()));
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();
    }

    @Override
    public boolean isInstall(Context context) {
        return WbSdk.isWbInstall(context);
    }

    @Override
    public ShareParamsHelper getParamsHelper() {
        return new SinaShareHelper();
    }

    @Override
    public void shareTo(ShareType type, Activity activity, Bundle bundle, ShareListener listener) {
        this.listener = listener;
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        SinaShareBean bean = bundle.getParcelable(SinaShareHelper.SINA_BUNDLE);
        switch (type) {
            case TEXT:
                weiboMessage.textObject = getTextObj(bean);
                break;
            case IMAGE:
                weiboMessage.imageObject = getImageObj(bean);
                break;
            case VIDEO:
                weiboMessage.videoSourceObject = getVideoObject(bean);
                break;
            case APP:
                //todo 此处为了测试 之后删掉
                weiboMessage.textObject = getTextObj(bean);
                weiboMessage.imageObject = getImageObj(bean);
                weiboMessage.videoSourceObject = getVideoObject(bean);
                break;
            case MULTIPLE:
                weiboMessage.textObject = getTextObj(bean);
                weiboMessage.imageObject = getImageObj(bean);
                weiboMessage.videoSourceObject = getVideoObject(bean);
                break;
            default:
                break;
        }
        shareHandler.shareMessage(weiboMessage, false);
    }

    private VideoSourceObject getVideoObject(SinaShareBean bean) {
        String filePath = bean.getAudioPath();
        if (TextUtils.isEmpty(filePath)) {
            return null;
        } else {
            VideoSourceObject videoSourceObject = new VideoSourceObject();
            videoSourceObject.videoPath = Uri.fromFile(new File(filePath));
            return videoSourceObject;
        }
    }

    private ImageObject getImageObj(SinaShareBean bean) {
        int imgUrl = bean.getResId();
        if (imgUrl == 0) {
            return null;
        } else {
            ImageObject imageObject = new ImageObject();
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgUrl);
            imageObject.setImageObject(bitmap);
            return imageObject;
        }
    }

    private TextObject getTextObj(SinaShareBean bean) {
        TextObject textObject = new TextObject();
        textObject.text = bean.getText();
        textObject.title = bean.getTitle();
        textObject.actionUrl = bean.getActionUrl();
        return textObject;
    }

    @Override
    public void onNewIntent(Intent intent) {
        shareHandler.doResultIntent(intent, this);
    }

    @Override
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {
    }

    @Override
    public void onWbShareSuccess() {
        listener.onComplete("share success!");
    }

    @Override
    public void onWbShareCancel() {
        listener.onCancel();
    }

    @Override
    public void onWbShareFail() {
        listener.onError("share failed");
    }
}
