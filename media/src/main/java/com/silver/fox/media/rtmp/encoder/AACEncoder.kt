package com.silver.fox.media.rtmp.encoder

import android.media.*
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer

class AACEncoder : Runnable {
    private val TAG = javaClass.simpleName
    private val mime = "audio/mp4a-latm"
    private var mRecorder: AudioRecord? = null
    private var mEnc: MediaCodec? = null
    var mediaFormat: MediaFormat? = null
        private set
    private val rate = 64000 //9600

    //录音设置
    private val sampleRate = 44100 //采样率，默认44.1k
    private val channelCount = 1 //音频采样通道，默认2通道
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO //通道设置，默认立体声
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT //设置采样数据格式，默认16比特PCM
    private var isRecording = false
    private var mThread: Thread? = null
    private var bufferSize = 0
    var audioEnncoderListener: AudioEnncoderListener? = null

    fun prepare() {
        //音频编码相关
        mediaFormat = MediaFormat.createAudioFormat(mime, sampleRate, channelCount).apply {
            setInteger(
                MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC
            )
            setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO)
            setInteger(MediaFormat.KEY_BIT_RATE, rate)
            val data = byteArrayOf(0x11.toByte(), 0x90.toByte())
            val mCSD0 = ByteBuffer.wrap(data)
            setByteBuffer("csd-0", mCSD0)
        }
        mEnc = MediaCodec.createEncoderByType(mime).apply {
            configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        }
        //音频录制相关
        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat) * 2
        mRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, sampleRate, channelConfig,
            audioFormat, bufferSize
        )
    }

    fun start() {
        mEnc?.start()
        mRecorder?.startRecording()
        if (mThread != null && mThread?.isAlive == true) {
            isRecording = false
            mThread?.join()
        }
        isRecording = true
        mThread = Thread(this)
        mThread?.start()
    }

    private fun getInputBuffer(index: Int): ByteBuffer? {
        return mEnc?.getInputBuffer(index)
    }

    private fun getOutputBuffer(index: Int): ByteBuffer? {
        return mEnc?.getOutputBuffer(index)
    }

    private val isWriteLocaAAC = false
    private fun readOutputData() {
        val index = mEnc!!.dequeueInputBuffer(-1)
        val pts = System.nanoTime() / 1000
        if (index >= 0) {
            val buffer = getInputBuffer(index)
            buffer!!.clear()
            val length = mRecorder!!.read(buffer, bufferSize)
            if (length > 0) {
                mEnc!!.queueInputBuffer(index, 0, length, pts, 0) //System.nanoTime() / 1000
            } else {
                Log.e(TAG, "length-->$length")
            }
        }
        val mInfo = MediaCodec.BufferInfo()
        var outIndex: Int
        do {
            outIndex = mEnc!!.dequeueOutputBuffer(mInfo, 0)
            if (outIndex >= 0) {
                val buffer = getOutputBuffer(outIndex)
                val outData = ByteArray(mInfo.size)
                buffer!![outData]
                if (audioEnncoderListener != null) {
                    audioEnncoderListener!!.getAudioBuffer(mEnc, outIndex, buffer, mInfo, outData)
                }
                mEnc!!.releaseOutputBuffer(outIndex, false)
            } else if (outIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
            } else if (outIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                Log.d(TAG, "readOutputData: " + mEnc!!.outputFormat)
                if (audioEnncoderListener != null) {
                    audioEnncoderListener!!.getOutputFormat(mEnc!!.outputFormat)
                }
            }
        } while (outIndex >= 0)
    }

    /**
     * 停止录制
     */
    fun stop() {
        try {
            isRecording = false
            mThread?.join()
            mRecorder?.stop()
            mEnc?.stop()
            mEnc?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        while (isRecording) {
            try {
                readOutputData()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    interface AudioEnncoderListener {
        fun getAudioBuffer(
            mediaCodec: MediaCodec?,
            status: Int,
            byteBuffer: ByteBuffer?,
            bufferInfo: MediaCodec.BufferInfo?,
            data: ByteArray?
        )

        fun getOutputFormat(format: MediaFormat?)
    }
}