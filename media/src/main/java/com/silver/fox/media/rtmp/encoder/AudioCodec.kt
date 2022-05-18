package com.silver.fox.media.rtmp.encoder

import android.media.*
import com.silver.fox.media.rtmp.RTMPPackage
import com.silver.fox.media.rtmp.ScreenLive
import java.io.IOException

class AudioCodec(private val screenLive: ScreenLive) : Thread() {
    var mediaCodec: MediaCodec? = null
    private var audioRecord: AudioRecord? = null
    var isRecording = false
    private var minBufferSize = 0
    var startTime: Long = 0
    fun startLive() {
        /**
         * 1、准备编码器
         */
        try {
            val mediaFormat = MediaFormat.createAudioFormat(
                MediaFormat.MIMETYPE_AUDIO_AAC,
                44100, 2
            )
            //编码规格，可以看成质量
            mediaFormat.setInteger(
                MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC
            )
            //码率
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 64000)
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
            mediaCodec!!.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //创建AudioRecord 录音
        //最小缓冲区大小
        minBufferSize = AudioRecord.getMinBufferSize(
            44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, minBufferSize
        )
        audioRecord!!.startRecording()
        start()
    }

    override fun run() {
        isRecording = true
        mediaCodec!!.start()

        //在获取播放的音频数据之前，先发送 audio Special config
        var rtmpPackage = RTMPPackage(byteArrayOf(0x12, 0x08), 0)
        rtmpPackage.type = RTMPPackage.RTMP_PACKET_TYPE_AUDIO_HEAD
        screenLive.addPackage(rtmpPackage)
        val buffer = ByteArray(minBufferSize)
        val bufferInfo = MediaCodec.BufferInfo()
        while (isRecording) {
            //得到采集的声音数据
            val len = audioRecord!!.read(buffer, 0, buffer.size)
            if (len <= 0) {
                continue
            }

            // 交给编码器编码

            //获取输入队列中能够使用的容器的下表
            var index = mediaCodec!!.dequeueInputBuffer(0)
            if (index >= 0) {
                val byteBuffer = mediaCodec!!.getInputBuffer(index)
                byteBuffer!!.clear()
                //把输入塞入容器
                byteBuffer.put(buffer, 0, len)

                //通知容器我们使用完了，你可以拿去编码了
                // 时间戳： 微秒， nano纳秒/1000
                mediaCodec!!.queueInputBuffer(index, 0, len, System.nanoTime() / 1000, 0)
            }


            // 获取编码之后的数据
            index = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 0)
            // 每次从编码器取完，再往编码器塞数据
            while (index >= 0 && isRecording) {
                val outputBuffer = mediaCodec!!.getOutputBuffer(index)
                val data = ByteArray(bufferInfo.size)
                outputBuffer!![data]
                if (startTime == 0L) {
                    startTime = bufferInfo.presentationTimeUs / 1000
                }

                //todo 送去推流
                rtmpPackage = RTMPPackage(data, bufferInfo.presentationTimeUs / 1000 - startTime)
                rtmpPackage.type = RTMPPackage.RTMP_PACKET_TYPE_AUDIO_DATA
                screenLive.addPackage(rtmpPackage)
                // 释放输出队列，让其能存放新数据
                mediaCodec!!.releaseOutputBuffer(index, false)
                index = mediaCodec!!.dequeueOutputBuffer(bufferInfo, 0)
            }
        }
        isRecording = false
    }
}