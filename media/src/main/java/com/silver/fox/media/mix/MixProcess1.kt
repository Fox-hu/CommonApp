package com.silver.fox.media.mix

import android.annotation.SuppressLint
import android.media.*
import android.util.Log
import com.silver.fox.common.InitApp
import com.silver.fox.media.audio.PcmToWav
import com.silver.fox.media.audio.pcm.PcmUtils
import com.silver.fox.media.audio.pcm.PcmEditor
import com.silver.fox.media.getTrackIndex
import java.io.File
import java.nio.ByteBuffer

class MixProcess1(
    private val audioInput: String,
    private val videoInput: String,
    private val output: String
) {

    private val videoExtractor: MediaExtractor =
        MediaExtractor().apply { setDataSource(videoInput) }
    private val mediaMuxer: MediaMuxer =
        MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

    companion object {
        const val TIMEOUT = 1000L
        const val TAG = "MixProcess"
    }

    fun mixAudioTrack(
        startTimeUs: Int, endTimeUs: Int,
        videoVolume: Int,
        aacVolume: Int
    ) {
        //1、分离出audio、video的pcm，生成文件
        var audioInputPcm: File? = null
        PcmEditor(audioInput).clip(startTimeUs, endTimeUs) {
            audioInputPcm = File(it)
        }

        var videoInputPcm: File? = null
        PcmEditor(videoInput).clip(startTimeUs, endTimeUs) {
            videoInputPcm = File(it)
        }
        Log.i(
            TAG,
            "生成pcm完毕，audioInputPcm = ${audioInputPcm?.absolutePath},videoInputPcm = ${videoInputPcm?.absolutePath}"
        )

        //2、将audio、video分离出的两个pcm文件合成为一个wav文件
        val adjustedPcm = File(
            "${InitApp.CONTEXT.filesDir?.parentFile?.path}",
            "mix_${System.currentTimeMillis()}.pcm"
        )
        //混合两个pcm文件
        PcmUtils.mixPcm(
            videoInputPcm?.absolutePath, audioInputPcm?.absolutePath,
            adjustedPcm.absolutePath, videoVolume, aacVolume
        )

        val wavFile =
            File("${InitApp.CONTEXT.filesDir?.parentFile?.path}",  "mix_${System.currentTimeMillis()}.wav")
        PcmToWav(
            44100, AudioFormat.CHANNEL_IN_STEREO,
            2, AudioFormat.ENCODING_PCM_16BIT
        ).pcmToWav(
            adjustedPcm.absolutePath, wavFile.absolutePath
        )
        Log.i(TAG, "mixAudioTrack: 转换完毕")

        mixVideoAndMusic(
            startTimeUs,
            endTimeUs,
            wavFile
        )
    }

    @SuppressLint("WrongConstant")
    private fun mixVideoAndMusic(
        startTimeUs: Int,
        endTimeUs: Int,
        wavFile: File
    ) {
        //获取输入mp4的视频轨和音频轨,分别添加进混合器中
        val videoIndex = videoExtractor.getTrackIndex("video/")
        val audioIndex = videoExtractor.getTrackIndex("audio/")

        val videoFormat = videoExtractor.getTrackFormat(videoIndex)
        mediaMuxer.addTrack(videoFormat)

        val audioFormat = videoExtractor.getTrackFormat(audioIndex)
        val audioBitrate = audioFormat.getInteger(MediaFormat.KEY_BIT_RATE)
        audioFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC)

        val muxerAudioIndex = mediaMuxer.addTrack(audioFormat)
        mediaMuxer.start()

        //处理输入的wav文件，转换成aac文件
        var audioTrack = 0
        val wavExtractor = MediaExtractor().apply {
            setDataSource(wavFile.absolutePath)
            audioTrack = getTrackIndex("audio/")
            selectTrack(audioTrack)
        }

        var maxBufferSize = if (audioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        } else {
            100 * 1000
        }

        val encodeFormat =
            MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 44100, 2).apply {
                setInteger(MediaFormat.KEY_BIT_RATE, audioBitrate)
                setInteger(
                    MediaFormat.KEY_AAC_PROFILE,
                    MediaCodecInfo.CodecProfileLevel.AACObjectLC
                )
                setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize)
            }
        val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC).apply {
            configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            start()
        }

        var buffer = ByteBuffer.allocateDirect(maxBufferSize)
        val info = MediaCodec.BufferInfo()

        var encodeDone = false
        while (!encodeDone) {
            val inputBufferIndex = encoder.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                val sampleTime = wavExtractor.sampleTime
                if (sampleTime < 0) {
                    //pts 小于零 说明到了文件末尾 通知编码器不用再编码了
                    encoder.queueInputBuffer(
                        inputBufferIndex,
                        0,
                        0,
                        0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                } else {
                    val sampleFlags = wavExtractor.sampleFlags
                    val size = wavExtractor.readSampleData(buffer, 0)
                    encoder.getInputBuffer(inputBufferIndex)?.apply {
                        clear()
                        put(buffer)
                        position(0)
                        encoder.queueInputBuffer(inputBufferIndex, 0, size, sampleTime, sampleFlags)
                        //读完这一帧 分离器继续读下一帧
                        wavExtractor.advance()
                    }
                }
            }

            var outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT)
            while (outputBufferIndex >= 0) {
                if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    encodeDone = true
                    break
                }
                encoder.getOutputBuffer(outputBufferIndex)?.apply {
                    //拿到编码后的数据后 写入到混合器中
                    mediaMuxer.writeSampleData(muxerAudioIndex, this, info)
                    clear()
                    encoder.releaseOutputBuffer(outputBufferIndex, false)
                    outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT)
                }
            }
        }

        //音频已经添加好了 继续添加视频
        //切换到视频轨道
        videoExtractor.apply {
            if (audioTrack >= 0) {
                unselectTrack(audioTrack)
            }
            selectTrack(videoIndex)
            seekTo(startTimeUs.toLong(), MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
            maxBufferSize = videoFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
            buffer = ByteBuffer.allocateDirect(maxBufferSize)
        }

        while (true) {
            val sampleTime = videoExtractor.sampleTime
            Log.i(TAG,"sampleTime = $sampleTime")
            if (sampleTime == -1L) {
                break
            }

            if (sampleTime < startTimeUs) {
                videoExtractor.advance()
                continue
            }
            if (sampleTime > endTimeUs) {
                break
            }
            //pts = 采样时间 - 初始时间 ，再添加一个延迟时间 600ms 可更改
            info.presentationTimeUs = sampleTime -startTimeUs
            info.flags = videoExtractor.sampleFlags
            //直接读取mp4文件中的视频数据 是已经压缩过的h264数据
            info.size = videoExtractor.readSampleData(buffer, 0)
            if (info.size < 0) {
                break
            }
            //写入混合器中
            mediaMuxer.writeSampleData(videoIndex, buffer, info)
            //分离器进行下一帧的分离
            videoExtractor.advance()

            try {
                wavExtractor.release()
                videoExtractor.release()
                encoder.stop()
                encoder.release()
                mediaMuxer.release()
            } catch (e: Exception) {

            }
        }
    }
}