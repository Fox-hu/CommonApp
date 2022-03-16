package com.silver.fox.media.rtmp

import java.util.concurrent.LinkedBlockingQueue

class CameraLive : Thread() {
    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    private var isLiving = false
    private val queue = LinkedBlockingQueue<RTMPPackage>()
    private var url: String? = null
    fun startLive(url: String?) {
        this.url = url
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
    external fun connect(url: String?): Boolean

    //发送RTMP Data
    external fun sendData(data: ByteArray?, len: Int, tms: Long, type: Int): Boolean

    //连接RTMP服务器
    external fun disconnect(): Boolean
    external fun readData(): ByteArray?
}