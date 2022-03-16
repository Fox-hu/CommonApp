package com.silver.fox.media.rtmp


import android.media.projection.MediaProjection
import com.silver.fox.media.rtmp.encoder.AudioCodec
import com.silver.fox.media.rtmp.encoder.VideoCodec
import java.util.concurrent.LinkedBlockingQueue

class ScreenLive : Thread() {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private var isLiving = false
    private val queue = LinkedBlockingQueue<RTMPPackage>()
    private var url: String? = null
    private var mediaProjection: MediaProjection? = null
    fun startLive(url: String?, mediaProjection: MediaProjection?) {
        this.url = url
        this.mediaProjection = mediaProjection
        LiveTaskManager.execute(this)
    }

    fun addPackage(rtmpPackage: RTMPPackage) {
        if (!isLiving) {
            return
        }
        queue.add(rtmpPackage)
    }

    override fun run() {
        //1推送到
        if (!connect(url)) {
            return
        }
        val videoCodec = VideoCodec(this)
        videoCodec.startLive(mediaProjection!!)
        val audioCodec = AudioCodec(this)
        audioCodec.startLive()
        isLiving = true
        while (isLiving) {
            var rtmpPackage: RTMPPackage? = null
            try {
                rtmpPackage = queue.take()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (rtmpPackage!!.buffer != null && rtmpPackage.buffer.isNotEmpty()) {
                sendData(
                    rtmpPackage.buffer,
                    rtmpPackage.buffer.size,
                    rtmpPackage.tms,
                    rtmpPackage.type
                )
            }
        }
    }

    //连接RTMP服务器
    private external fun connect(url: String?): Boolean

    //发送RTMP Data
    private external fun sendData(data: ByteArray, len: Int, tms: Long, type: Int): Boolean
}