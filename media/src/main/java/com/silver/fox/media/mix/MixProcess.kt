package com.silver.fox.media.mix

import android.annotation.SuppressLint
import android.media.*
import android.util.Log
import com.silver.fox.common.InitApp
import com.silver.fox.media.audio.PcmToWav
import com.silver.fox.media.audio.pcm.PcmUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

object MixProcess {
    private const val TAG = "MusicProcess"
    private const val TIMEOUT = 1000
    //将一个视频和一个音频进行混合
    fun mixAudioTrack(
        videoInput: String?,
        audioInput: String?,
        output: String,
        startTimeUs: Int, endTimeUs: Int,
        videoVolume: Int,
        aacVolume: Int
    ) {
        //1.将mp3和mp4解封装，转换为pcm格式，合并转换后的pcm，封装为wav格式
        val cacheDir = File("${InitApp.CONTEXT.filesDir?.parentFile?.path}")
        val aacPcmFile = File(cacheDir, "audio" + ".pcm")
        val videoPcmFile = File(cacheDir, "video" + ".pcm")
        val audioExtractor = MediaExtractor()
        audioExtractor.setDataSource(audioInput!!)
        //抽取mp4的音频 转为video.pcm
        decodeToPCM(videoInput, videoPcmFile.absolutePath,
            startTimeUs, endTimeUs
        )
        //抽取mp3的音频 转为audio.pcm
        decodeToPCM(audioInput, aacPcmFile.absolutePath, startTimeUs, endTimeUs)
        //将audio.pcm和video.pcm 混合后 转为.wav格式 注意 此时并未进行编码
        val adjustedPcm = File(cacheDir, "混合后的" + ".pcm")
        PcmUtils.mixPcm(
            videoPcmFile.absolutePath, aacPcmFile.absolutePath,
            adjustedPcm.absolutePath, videoVolume, aacVolume
        )
        val wavFile = File(cacheDir, adjustedPcm.name + ".wav")
        PcmToWav(
            44100, AudioFormat.CHANNEL_IN_STEREO,
            2, AudioFormat.ENCODING_PCM_16BIT
        ).pcmToWav(
            adjustedPcm.absolutePath, wavFile.absolutePath
        )
        Log.i(TAG, "mixAudioTrack: 转换完毕")
        //2.将wav文件 与mp4文件 合成为新的out.mp4文件
        mixVideoAndMusic(videoInput, output, startTimeUs, endTimeUs, wavFile)
    }

    private fun mixVideoAndMusic(
        videoInput: String?,
        output: String,
        startTimeUs: Int,
        endTimeUs: Int?,
        wavFile: File
    ) {
        //1、设置合成器，确定合成器两个轨道的各项参数，像轨道中分别写入视频和音频数据
        // 将输入的mp4和wav文件重新封装成一个mp4文件
        val mediaMuxer = MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        // 设置视频输入轨道
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(videoInput!!)
        // 拿到视频轨道的索引
        val videoIndex = selectTrack(mediaExtractor, false)
        val audioIndex = selectTrack(mediaExtractor, true)


        // 视频配置 文件
        val videoFormat = mediaExtractor.getTrackFormat(videoIndex)
        // 开辟空的轨道写视频数据
        mediaMuxer.addTrack(videoFormat)

        // 音频的数据已准备好 由wav文件提供数据 音频各项参数取自于原mp4的音频参数
        val audioFormat = mediaExtractor.getTrackFormat(audioIndex)
        val audioBitrate = audioFormat.getInteger(MediaFormat.KEY_BIT_RATE)
        audioFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC)
        // 开辟空轨道写音频数据 轨道格式取自原mp4文件 与原mp4文件保持一致
        val muxerAudioIndex = mediaMuxer.addTrack(audioFormat)

        // 音频、视频两个轨道开辟完成  输出开始工作
        mediaMuxer.start()

        //2、解wav文件封装，编码为aac数据，写入合成器的音频轨道
        val pcmExtrator = MediaExtractor()
        pcmExtrator.setDataSource(wavFile.absolutePath)
        val audioTrack = selectTrack(pcmExtrator, true)
        pcmExtrator.selectTrack(audioTrack)
        val pcmTrackFormat = pcmExtrator.getTrackFormat(audioTrack)

        //最大一帧的大小
        var maxBufferSize = 0
        maxBufferSize = if (audioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            pcmTrackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        } else {
            100 * 1000
        }

        // 设置编码器
        val encodeFormat = MediaFormat.createAudioFormat(
            MediaFormat.MIMETYPE_AUDIO_AAC,
            44100, 2
        ) //参数对应-> mime type、采样率、声道数
        encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, audioBitrate) //比特率
        // 音质等级
        encodeFormat.setInteger(
            MediaFormat.KEY_AAC_PROFILE,
            MediaCodecInfo.CodecProfileLevel.AACObjectLC
        )
        encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize)
        val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        //重新编码 将pcm数据编码为aac数据
        encoder.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        encoder.start()

        var buffer = ByteBuffer.allocateDirect(maxBufferSize)
        val info = MediaCodec.BufferInfo()
        var encodeDone = false
        while (!encodeDone) {
            val inputBufferIndex = encoder.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                val sampleTime = pcmExtrator.sampleTime
                if (sampleTime < 0) {
                //pts小于0  来到了文件末尾 通知编码器  不用编码了
                    encoder.queueInputBuffer(
                        inputBufferIndex,
                        0,
                        0,
                        0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                } else {
                    val flags = pcmExtrator.sampleFlags
                    val size = pcmExtrator.readSampleData(buffer, 0)
                    val inputBuffer = encoder.getInputBuffer(inputBufferIndex)
                    inputBuffer!!.clear()
                    inputBuffer.put(buffer)
                    inputBuffer.position(0)
                    encoder.queueInputBuffer(inputBufferIndex, 0, size, sampleTime, flags)
                    // 读完这一帧 继续分离wav文件下一帧音频数据
                    pcmExtrator.advance()
                    Log.i(TAG,"写入一帧音频数据成功 , size = $size, sampleTime = $sampleTime")
                }
            }
            // 获取编码完的数据
            var outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT.toLong())
            while (outputBufferIndex >= 0) {
                if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    encodeDone = true
                    break
                }
                val encodeOutputBuffer = encoder.getOutputBuffer(outputBufferIndex)
                // 将编码好的acc数据写入合成器的音频轨道中
                mediaMuxer.writeSampleData(muxerAudioIndex, encodeOutputBuffer!!, info)
                encodeOutputBuffer.clear()
                encoder.releaseOutputBuffer(outputBufferIndex, false)
                outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT.toLong())
            }
        }
        // 以上 就把合成器中的音频轨道数据添加好了
        Log.i(TAG,"写入全部音频数据成功")
        // 继续向合成器中添加视频轨道数据
        if (audioTrack >= 0) {
            mediaExtractor.unselectTrack(audioTrack)
        }
        //选择mp4中的视频轨道
        mediaExtractor.selectTrack(videoIndex)
        mediaExtractor.seekTo(startTimeUs.toLong(), MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        maxBufferSize = videoFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        buffer = ByteBuffer.allocateDirect(maxBufferSize)
        //向合成器中添加视频轨道信息 无需解码 直接添加h264数据
        while (true) {
            val sampleTimeUs = mediaExtractor.sampleTime
            if (sampleTimeUs == -1L) {
                break
            }
            if (sampleTimeUs < startTimeUs) {
                mediaExtractor.advance()
                continue
            }
            if (endTimeUs != null && sampleTimeUs > endTimeUs) {
                break
            }
            // 这里的pts就不是原来的sampleTimeUs了 需要 - 裁剪开始时间 + 余量 此处取600Us
            info.presentationTimeUs = sampleTimeUs - startTimeUs + 600
            info.flags = mediaExtractor.sampleFlags
            // 使用解封装器 读取视频文件的视频数据 h264格式 写入buffer中
            info.size = mediaExtractor.readSampleData(buffer, 0)
            if (info.size < 0) {
                break
            }
            // 使用buffer 向合成器中视频轨道写入视频数据
            mediaMuxer.writeSampleData(videoIndex, buffer, info)
            // 解封装器继续读取下一帧的内容
            mediaExtractor.advance()
            Log.i(TAG,"写入一帧视频数据成功 , size = ${info.size} pts = ${info.presentationTimeUs}")
        }
        Log.i(TAG,"写入全部视频数据成功")
        try {
            pcmExtrator.release()
            mediaExtractor.release()
            encoder.stop()
            encoder.release()
            mediaMuxer.release()
        } catch (e: Exception) {
        }
    }

    //    从一个MP3文件中截取并且输出pcm文件
    @SuppressLint("WrongConstant")
    fun decodeToPCM(musicPath: String?, outPath: String?, startTime: Int, endTime: Int) {
        if (endTime < startTime) {
            return
        }
        // 先用解封装器 将mp3文件中的音频文件分离出来
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(musicPath!!)
        val audioTrack = selectTrack(mediaExtractor, true)
        mediaExtractor.selectTrack(audioTrack)
        mediaExtractor.seekTo(startTime.toLong(), MediaExtractor.SEEK_TO_CLOSEST_SYNC)
        // 轨道信息  都记录 编码器
        val audioFormat = mediaExtractor.getTrackFormat(audioTrack)
        var maxBufferSize = 100 * 1000
        maxBufferSize = if (audioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            audioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        } else {
            100 * 1000
        }
        val buffer = ByteBuffer.allocateDirect(maxBufferSize)
        // 创建解码器 将aac 解码为pcm文件
        val mediaCodec = MediaCodec.createDecoderByType(
            audioFormat
                .getString(MediaFormat.KEY_MIME)
        )
        // 设置解码器信息
        mediaCodec.configure(audioFormat, null, null, 0)
        val pcmFile = File(outPath)
        val writeChannel = FileOutputStream(pcmFile).channel
        // 开始解码
        mediaCodec.start()

        val info = MediaCodec.BufferInfo()
        var outputBufferIndex = -1
        while (true) {
            val decodeInputIndex = mediaCodec.dequeueInputBuffer(1000)
            if (decodeInputIndex >= 0) {
                val sampleTimeUs = mediaExtractor.sampleTime
                if (sampleTimeUs == -1L) {
                    break
                } else if (sampleTimeUs < startTime) {
                    // 不在裁剪时间内 舍弃不处理 解封装器继续处理下一帧
                    mediaExtractor.advance()
                    continue
                } else if (sampleTimeUs > endTime) {
                    break
                }
                // 从解封装器中 获取到压缩数据
                info.size = mediaExtractor.readSampleData(buffer, 0)
                info.presentationTimeUs = sampleTimeUs
                info.flags = mediaExtractor.sampleFlags

                // 放入解码器输入队列
                val content = ByteArray(buffer.remaining())
                buffer[content]
                val inputBuffer = mediaCodec.getInputBuffer(decodeInputIndex)
                inputBuffer!!.put(content)
                mediaCodec.queueInputBuffer(
                    decodeInputIndex,
                    0,
                    info.size,
                    info.presentationTimeUs,
                    info.flags
                )
                // 解封装器继续处理下一帧数据
                mediaExtractor.advance()
            }
            //获取解码后的数据
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 1000)
            while (outputBufferIndex >= 0) {
                val decodeOutputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex)
                // 写入到输出文件
                writeChannel.write(decodeOutputBuffer)
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 1000)
            }
        }
        writeChannel.close()
        mediaExtractor.release()
        mediaCodec.stop()
        mediaCodec.release()
        Log.i(TAG,"mp3 $musicPath 转 pcm: $outPath 转换完毕")
    }

    private fun selectTrack(extractor: MediaExtractor, audio: Boolean): Int {
        val numTracks = extractor.trackCount
        for (i in 0 until numTracks) {
            val format = extractor.getTrackFormat(i)
            val mime = format.getString(MediaFormat.KEY_MIME)
            if (audio) {
                if (mime.startsWith("audio/")) {
                    return i
                }
            } else {
                if (mime.startsWith("video/")) {
                    return i
                }
            }
        }
        return -5
    }
}