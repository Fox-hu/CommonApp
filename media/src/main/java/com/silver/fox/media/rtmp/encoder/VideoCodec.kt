package com.silver.fox.media.rtmp.encoder

import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.os.Bundle
import com.silver.fox.media.rtmp.LiveTaskManager
import com.silver.fox.media.rtmp.RTMPPackage
import com.silver.fox.media.rtmp.ScreenLive
import java.io.IOException

class VideoCodec(private val screenLive: ScreenLive) : Thread() {
    private var mediaProjection: MediaProjection? = null
    private var mediaCodec //硬编
            : MediaCodec? = null
    private var isLiving = false
    private var virtualDisplay //虚拟画布
            : VirtualDisplay? = null
    private var timeStamp: Long = 0
    private var startTime: Long = 0
    fun startLive(mediaProjection: MediaProjection) {
        this.mediaProjection = mediaProjection
        val format = MediaFormat.createVideoFormat(
            MediaFormat.MIMETYPE_VIDEO_AVC,
            720,
            1280
        )
        format.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        //码率，帧率，分辨率，关键帧间隔
        format.setInteger(MediaFormat.KEY_BIT_RATE, 400000)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 15)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC) //手机
            mediaCodec!!.configure(
                format, null, null,
                MediaCodec.CONFIGURE_FLAG_ENCODE
            )
            val surface = mediaCodec!!.createInputSurface()
            virtualDisplay = mediaProjection.createVirtualDisplay(
                "screen-codec",
                720, 1280, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                surface, null, null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        LiveTaskManager.execute(this)
    }

    override fun run() {
        isLiving = true
        mediaCodec!!.start()
        val bufferInfo = MediaCodec.BufferInfo()
        while (isLiving) {
            //2000毫秒 手动触发输出关键帧
            if (System.currentTimeMillis() - timeStamp >= 2000) {
                val params = Bundle()
                //立即刷新 让下一帧是关键帧
                params.putInt(MediaCodec.PARAMETER_KEY_REQUEST_SYNC_FRAME, 0)
                mediaCodec?.setParameters(params)
                timeStamp = System.currentTimeMillis()
            }
            val index = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 100000)
            if (index >= 0) {
                val buffer = mediaCodec!!.getOutputBuffer(index)
                val mediaFormat = mediaCodec!!.getOutputFormat(index)
                val outData = ByteArray(bufferInfo.size)
                buffer!![outData]
                if (startTime == 0L) {
                    // 微妙转为毫秒
                    startTime = bufferInfo.presentationTimeUs / 1000
                }
                //                包含   分隔符
                val rtmpPackage =
                    RTMPPackage(outData, bufferInfo.presentationTimeUs / 1000 - startTime)
                rtmpPackage.type = RTMPPackage.RTMP_PACKET_TYPE_VIDEO
                screenLive.addPackage(rtmpPackage)
                mediaCodec!!.releaseOutputBuffer(index, false)
            }
        }
        isLiving = false
        startTime = 0
        mediaCodec!!.stop()
        mediaCodec!!.release()
        mediaCodec = null
        virtualDisplay!!.release()
        virtualDisplay = null
        mediaProjection!!.stop()
        mediaProjection = null
    }
}