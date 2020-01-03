package com.fox.authandshare.share.wechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.fox.authandshare.Util;
import com.fox.authandshare.platform.PlatForm;
import com.fox.authandshare.share.IShare;
import com.fox.authandshare.share.ShareListener;
import com.fox.authandshare.share.ShareParamsHelper;
import com.fox.authandshare.share.ShareType;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;


/**
 * Created by fox.hu on 2018/9/7.
 */

public class WeChatShare implements IShare {
    private static final String TAG = WeChatShare.class.getSimpleName();
    private IWXAPI api;
    private ShareListener listener;
    private int targetScene = SendMessageToWX.Req.WXSceneSession;
    private static final int THUMB_SIZE = 150;
    private Activity activity;


    public WeChatShare(Activity activity) {
        this.activity = activity;
        api = WXAPIFactory.createWXAPI(activity.getApplicationContext(),
                PlatForm.WEIXIN.getAppId());
        api.registerApp(PlatForm.WEIXIN.getAppId());
    }

    @Override
    public boolean isInstall(Context context) {
        return api.isWXAppInstalled();
    }

    @Override
    public ShareParamsHelper getParamsHelper() {
        return new WeChatShareHelper();
    }

    @Override
    public void shareTo(ShareType type, Activity activity, Bundle bundle, ShareListener listener) {
        this.listener = listener;
        WeChatShareBean bean = bundle.getParcelable(WeChatShareHelper.WE_CHAT_BUNDLE);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        switch (type) {
            case TEXT:
                getTextObj(req, bean);
                break;
            case IMAGE:
                //图片太大时无法进行分享
                getImgObj(req, bean);
                break;
            case MP3:
            case APP:
                getMp3Obj(req, bean);
                break;
            case VIDEO:
                getVideoObj(req, bean);
                break;
            default:
                break;
        }
        api.sendReq(req);
    }

    private void getVideoObj(SendMessageToWX.Req req, WeChatShareBean bean) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = "http://www.qq.com";

        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = bean.getMediaTitle();
        msg.description = bean.getMediaDescr();

        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), bean.getThumbImgId());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = targetScene;
    }

    private void getMp3Obj(SendMessageToWX.Req req, WeChatShareBean bean) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = bean.getMp3Url();

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = bean.getMediaTitle();
        msg.description = bean.getMediaDescr();

        Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(), bean.getThumbImgId());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = targetScene;
    }

    private void getImgObj(SendMessageToWX.Req req, WeChatShareBean bean) {
        File file = new File(bean.getImgPath());
        if (!file.exists()) {
            listener.onError("no file");
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(bean.getImgPath());

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(bean.getImgPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = targetScene;
    }

    private void getTextObj(SendMessageToWX.Req req, WeChatShareBean bean) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = bean.getText();

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = bean.getText();

        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = targetScene;
    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResultData(int requestCode, int resultCode, Intent data) {

    }

    private String buildTransaction(final String type) {
        return (type == null)
               ? String.valueOf(System.currentTimeMillis())
               : type + System.currentTimeMillis();
    }
}
