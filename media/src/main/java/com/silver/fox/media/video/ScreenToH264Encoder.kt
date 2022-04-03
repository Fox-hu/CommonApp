package com.silver.fox.media.video

import android.hardware.display.DisplayManager
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.view.Surface

data class EncoderParam(val width: Int, val height: Int)

class ScreenToH264Encoder(
    private val param: EncoderParam,
    private val mediaProjection: MediaProjection,
    private val action: (ByteArray) -> Unit
) :
    Runnable {

    private var mediaCodec: MediaCodec? = null
    private var surface: Surface? = null

    @Volatile
    private var isEncoding = false

    private fun initEncoder() {
        try {
            mediaCodec = MediaCodec.createEncoderByType("video/avc")
            val format = MediaFormat.createVideoFormat(
                MediaFormat.MIMETYPE_VIDEO_AVC,
                param.width,
                param.height
            ).apply {
                setInteger(
                    MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
                )
                setInteger(MediaFormat.KEY_FRAME_RATE, 15)
                setInteger(MediaFormat.KEY_BIT_RATE, 400000)
                setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2) //2s一个I帧
            }
            mediaCodec?.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            surface = mediaCodec?.createInputSurface()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        mediaCodec?.apply {
            start()
            //这里设置编码器输入为mediaProjection
            mediaProjection.createVirtualDisplay(
                "screen-codec",
                param.width, param.height, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                surface, null, null
            )
            val bufferInfo = MediaCodec.BufferInfo()
            while (isEncoding) {
                //获取编码器输出
                val index = dequeueOutputBuffer(bufferInfo, 100000)
                if (index >= 0) {
                    //获取输出的buffer
                    val buffer = getOutputBuffer(index)
                    //将输入的buffer中的数据写入outData中
                    val outData = ByteArray(bufferInfo.size)
                    buffer?.get(outData)
                    action(outData)
                    releaseOutputBuffer(index, false)
                }
            }
        }
    }

    fun start(){
        isEncoding = true
        initEncoder()
        Thread(this).start()
    }

    fun stop(){
        isEncoding = false
    }
}