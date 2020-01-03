package com.fox.authandshare.share.wechat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.fox.authandshare.platform.PlatForm
import com.fox.authandshare.share.IShare
import com.fox.authandshare.share.ShareListener
import com.fox.authandshare.share.ShareParamsHelper
import com.fox.authandshare.share.ShareType
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.File


/**
 * Created by fox.hu on 2018/9/7.
 */

class WeChatShare(private val activity: Activity) : IShare {
    private val api: IWXAPI
    private var listener: ShareListener? = null
    private val targetScene = SendMessageToWX.Req.WXSceneSession

    override val paramsHelper: ShareParamsHelper<*>
        get() = WeChatShareHelper()


    init {
        api = WXAPIFactory.createWXAPI(
            activity.applicationContext,
            PlatForm.WEIXIN.appId
        )
        api.registerApp(PlatForm.WEIXIN.appId)
    }

    override fun isInstall(context: Context): Boolean {
        return api.isWXAppInstalled
    }

    override fun shareTo(
        type: ShareType,
        activity: Activity,
        bundle: Bundle,
        listener: ShareListener
    ) {
        this.listener = listener
        val bean = bundle.getParcelable<WeChatShareBean>(WeChatShareHelper.WE_CHAT_BUNDLE)
        val req = SendMessageToWX.Req()
        when (type) {
            ShareType.TEXT -> getTextObj(req, bean!!)
            ShareType.IMAGE ->
                //图片太大时无法进行分享
                getImgObj(req, bean!!)
            ShareType.MP3, ShareType.APP -> getMp3Obj(req, bean!!)
            ShareType.VIDEO -> getVideoObj(req, bean!!)
            else -> {
            }
        }
        api.sendReq(req)
    }

    private fun getVideoObj(req: SendMessageToWX.Req, bean: WeChatShareBean) {
        val video = WXVideoObject()
        video.videoUrl = "http://www.qq.com"

        val msg = WXMediaMessage(video)
        msg.title = bean.mediaTitle
        msg.description = bean.mediaDescr

        val bmp = BitmapFactory.decodeResource(activity.resources, bean.thumbImgId)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        req.transaction = buildTransaction("video")
        req.message = msg
        req.scene = targetScene
    }

    private fun getMp3Obj(req: SendMessageToWX.Req, bean: WeChatShareBean) {
        val music = WXMusicObject()
        music.musicUrl = bean.mp3Url

        val msg = WXMediaMessage()
        msg.mediaObject = music
        msg.title = bean.mediaTitle
        msg.description = bean.mediaDescr

        val bmp = BitmapFactory.decodeResource(activity.resources, bean.thumbImgId)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        req.transaction = buildTransaction("music")
        req.message = msg
        req.scene = targetScene
    }

    private fun getImgObj(req: SendMessageToWX.Req, bean: WeChatShareBean) {
        val file = File(bean.imgPath!!)
        if (!file.exists()) {
            listener!!.onError("no file")
            return
        }

        val imgObj = WXImageObject()
        imgObj.setImagePath(bean.imgPath)

        val msg = WXMediaMessage()
        msg.mediaObject = imgObj

        val bmp = BitmapFactory.decodeFile(bean.imgPath)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true)

        req.transaction = buildTransaction("img")
        req.message = msg
        req.scene = targetScene
    }

    private fun getTextObj(req: SendMessageToWX.Req, bean: WeChatShareBean) {
        val textObj = WXTextObject()
        textObj.text = bean.text

        val msg = WXMediaMessage()
        msg.mediaObject = textObj
        msg.description = bean.text

        req.transaction = buildTransaction("text")
        req.message = msg
        req.scene = targetScene
    }

    override fun onNewIntent(intent: Intent) {

    }

    override fun onActivityResultData(requestCode: Int, resultCode: Int, data: Intent) {

    }

    private fun buildTransaction(type: String?): String {
        return if (type == null)
            System.currentTimeMillis().toString()
        else
            type + System.currentTimeMillis()
    }

    companion object {
        private val TAG = WeChatShare::class.java.simpleName
        private val THUMB_SIZE = 150
    }
}
