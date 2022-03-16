package com.silver.fox.media.rtmp.encoder

import android.hardware.Camera
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import java.nio.ByteBuffer

class AVCEncoder(private val camera: Camera) : Runnable {
    private var isVideoing = true
    private var videomediaCodec: MediaCodec? = null
    private var mThread: Thread? = null
    private var videoEnncoderListener: VideoEnncoderListener? = null
    private lateinit var datas: ByteArray
    private val handlerThread = HandlerThread("video2")
    private var handler: Handler? = null
    private val timeStamp: Long = 0
    fun setVideoEnncoderListener(videoEnncoderListener: VideoEnncoderListener?) {
        this.videoEnncoderListener = videoEnncoderListener
    }

    fun prepare(width: Int, height: Int) {
        val format = MediaFormat.createVideoFormat("video/avc", width, height)
        //色彩空间
        format.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar
        )
        //码率
        format.setInteger(MediaFormat.KEY_BIT_RATE, 400000)
        //帧率
        format.setInteger(MediaFormat.KEY_FRAME_RATE, 15)
        //关键帧间隔
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)
        videomediaCodec = MediaCodec.createEncoderByType("video/avc")
        videomediaCodec!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
    }

    fun start() {
        handlerThread.start()
        handler = object : Handler(handlerThread.looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    0 -> readOutputData()
                }
            }
        }
        videomediaCodec!!.start()
        if (mThread != null && mThread!!.isAlive) {
            isVideoing = false
            mThread!!.join()
        }
        isVideoing = true
        mThread = Thread(this)
        mThread!!.start()
    }

    private fun getInputBuffer(index: Int): ByteBuffer? {
        return videomediaCodec?.getInputBuffer(index)
    }

    private fun getOutputBuffer(index: Int): ByteBuffer? {
        return videomediaCodec?.getOutputBuffer(index)
    }

    private fun readOutputData() {
        val index = videomediaCodec!!.dequeueInputBuffer(0)
        if (index >= 0) {
            val inputBuffer = videomediaCodec?.getInputBuffer(index)
            inputBuffer?.clear()
            inputBuffer?.put(datas, 0, datas.size)
            //填充数据后再加入队列
            videomediaCodec?.queueInputBuffer(index, 0, datas.size, System.nanoTime() / 1000, 0)
        }
        while (true) {
            //获得输出缓冲区（编码后的数据从输出缓冲区获得）
            val bufferInfo = MediaCodec.BufferInfo()
            val encoderStatus = videomediaCodec!!.dequeueOutputBuffer(bufferInfo, 0)
            if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                //稍后重试
                break
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                if (videoEnncoderListener != null) {
                    videoEnncoderListener!!.getOutputFormat(videomediaCodec!!.outputFormat)
                }
            } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                //可以忽略
            } else {
                val encodedData = videomediaCodec!!.getOutputBuffer(encoderStatus)
                val outData = ByteArray(bufferInfo.size)
                encodedData!![outData]
                videomediaCodec!!.releaseOutputBuffer(encoderStatus, false)
                if (videoEnncoderListener != null) {
                    videoEnncoderListener!!.getVideoBuffer(
                        videomediaCodec,
                        encoderStatus,
                        encodedData,
                        bufferInfo,
                        outData
                    )
                }
            }
        }
    }

    fun stop() {
        try {
            isVideoing = false
            mThread!!.join()
            videomediaCodec!!.stop()
            videomediaCodec!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        camera.setPreviewCallback { data, camera ->
            if (isVideoing) {
                datas = data
                val message = Message()
                message.what = 0
                handler!!.sendMessage(message)
            }
        }
    }

    interface VideoEnncoderListener {
        fun getVideoBuffer(
            mediaCodec: MediaCodec?,
            encoderStatus: Int,
            byteBuffer: ByteBuffer?,
            bufferInfo: MediaCodec.BufferInfo?,
            data: ByteArray
        )

        fun getOutputFormat(format: MediaFormat?)
    }
}