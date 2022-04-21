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
    fun mixAudioTrack(
        videoInput: String?,
        audioInput: String?,
        output: String,
        startTimeUs: Int, endTimeUs: Int,
        videoVolume: Int,
        aacVolume: Int
    ) {
        val cacheDir = File("${InitApp.CONTEXT.filesDir?.parentFile?.path}")
        //        下载下来的音乐转换城pcm
        val aacPcmFile = File(cacheDir, "audio" + ".pcm")
        //        视频自带的音乐转换城pcm
        val videoPcmFile = File(cacheDir, "video" + ".pcm")
        val audioExtractor = MediaExtractor()
        audioExtractor.setDataSource(audioInput!!)
        decodeToPCM(
            videoInput, videoPcmFile.absolutePath,
            startTimeUs, endTimeUs
        )
        decodeToPCM(audioInput, aacPcmFile.absolutePath, startTimeUs, endTimeUs)
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
        //混音的wav文件   + 视频文件   ---》  生成
        mixVideoAndMusic(videoInput, output, startTimeUs, endTimeUs, wavFile)
    }

    private fun mixVideoAndMusic(
        videoInput: String?,
        output: String,
        startTimeUs: Int,
        endTimeUs: Int?,
        wavFile: File
    ) {


        //        初始化一个视频封装容器
        val mediaMuxer = MediaMuxer(output, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

//            一个轨道    既可以装音频 又视频   是 1 不是2
//            取音频轨道  wav文件取配置信息
//            先取视频
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(videoInput!!)
        //            拿到视频轨道的索引
        val videoIndex = selectTrack(mediaExtractor, false)
        val audioIndex = selectTrack(mediaExtractor, true)


//            视频配置 文件
        val videoFormat = mediaExtractor.getTrackFormat(videoIndex)
        //开辟了一个 轨道   空的轨道   写数据     真实
        mediaMuxer.addTrack(videoFormat)

//        ------------音频的数据已准备好----------------------------
//            视频中音频轨道   应该取自于原视频的音频参数
        val audioFormat = mediaExtractor.getTrackFormat(audioIndex)
        val audioBitrate = audioFormat.getInteger(MediaFormat.KEY_BIT_RATE)
        audioFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_AUDIO_AAC)
        //        添加一个空的轨道  轨道格式取自 视频文件，跟视频所有信息一样
        val muxerAudioIndex = mediaMuxer.addTrack(audioFormat)


//            音频轨道开辟好了  输出开始工作
        mediaMuxer.start()

//音频的wav
        val pcmExtrator = MediaExtractor()
        pcmExtrator.setDataSource(wavFile.absolutePath)
        val audioTrack = selectTrack(pcmExtrator, true)
        pcmExtrator.selectTrack(audioTrack)
        val pcmTrackFormat = pcmExtrator.getTrackFormat(audioTrack)


        //最大一帧的 大小
        var maxBufferSize = 0
        maxBufferSize = if (audioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
            pcmTrackFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        } else {
            100 * 1000
        }


//    最终输出   后面   混音   -----》     重采样   混音     这个下节课讲
        val encodeFormat = MediaFormat.createAudioFormat(
            MediaFormat.MIMETYPE_AUDIO_AAC,
            44100, 2
        ) //参数对应-> mime type、采样率、声道数
        encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, audioBitrate) //比特率
        //            音质等级
        encodeFormat.setInteger(
            MediaFormat.KEY_AAC_PROFILE,
            MediaCodecInfo.CodecProfileLevel.AACObjectLC
        )
        //            解码  那段
        encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxBufferSize)
        //解码 那
        val encoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        //            配置AAC 参数  编码 pcm   重新编码     视频文件变得更小
        encoder.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        encoder.start()
        //            容器
        var buffer = ByteBuffer.allocateDirect(maxBufferSize)
        val info = MediaCodec.BufferInfo()
        var encodeDone = false
        while (!encodeDone) {
            val inputBufferIndex = encoder.dequeueInputBuffer(10000)
            if (inputBufferIndex >= 0) {
                val sampleTime = pcmExtrator.sampleTime
                if (sampleTime < 0) {
//                        pts小于0  来到了文件末尾 通知编码器  不用编码了
                    encoder.queueInputBuffer(
                        inputBufferIndex,
                        0,
                        0,
                        0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                } else {
                    val flags = pcmExtrator.sampleFlags
                    //
                    val size = pcmExtrator.readSampleData(buffer, 0)
                    //                    编辑     行 1 还是不行 2   不要去用  空的
                    val inputBuffer = encoder.getInputBuffer(inputBufferIndex)
                    inputBuffer!!.clear()
                    inputBuffer.put(buffer)
                    inputBuffer.position(0)
                    encoder.queueInputBuffer(inputBufferIndex, 0, size, sampleTime, flags)
                    //                        读完这一帧
                    pcmExtrator.advance()
                }
            }
            //                获取编码完的数据
            var outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT.toLong())
            while (outputBufferIndex >= 0) {
                if (info.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                    encodeDone = true
                    break
                }
                val encodeOutputBuffer = encoder.getOutputBuffer(outputBufferIndex)
                //                    将编码好的数据  压缩 1     aac
                mediaMuxer.writeSampleData(muxerAudioIndex, encodeOutputBuffer!!, info)
                encodeOutputBuffer.clear()
                encoder.releaseOutputBuffer(outputBufferIndex, false)
                outputBufferIndex = encoder.dequeueOutputBuffer(info, TIMEOUT.toLong())
            }
        }
        //    把音频添加好了
        if (audioTrack >= 0) {
            mediaExtractor.unselectTrack(audioTrack)
        }
        //视频
        mediaExtractor.selectTrack(videoIndex)
        mediaExtractor.seekTo(startTimeUs.toLong(), MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        maxBufferSize = videoFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        buffer = ByteBuffer.allocateDirect(maxBufferSize)
        //封装容器添加视频轨道信息
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
            //                pts      0
            info.presentationTimeUs = sampleTimeUs - startTimeUs + 600
            info.flags = mediaExtractor.sampleFlags
            //                读取视频文件的数据  画面 数据   压缩1  未压缩2
            info.size = mediaExtractor.readSampleData(buffer, 0)
            if (info.size < 0) {
                break
            }
            //                视频轨道  画面写完了
            mediaMuxer.writeSampleData(videoIndex, buffer, info)
            mediaExtractor.advance()
        }
        try {
            pcmExtrator.release()
            mediaExtractor.release()
            encoder.stop()
            encoder.release()
            mediaMuxer.release()
        } catch (e: Exception) {
        }
    }

    //    MP3 截取并且输出  pcm
    @SuppressLint("WrongConstant")
    fun decodeToPCM(musicPath: String?, outPath: String?, startTime: Int, endTime: Int) {
        if (endTime < startTime) {
            return
        }
        //    MP3  （zip  rar    ） ----> aac   封装个事 1   编码格式
//        jie  MediaExtractor = 360 解压 工具
        val mediaExtractor = MediaExtractor()
        mediaExtractor.setDataSource(musicPath!!)
        val audioTrack = selectTrack(mediaExtractor, true)
        mediaExtractor.selectTrack(audioTrack)
        // 视频 和音频
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
        //        h264   H265  音频
        val mediaCodec = MediaCodec.createDecoderByType(
            audioFormat
                .getString(MediaFormat.KEY_MIME)
        )
        //        设置解码器信息    直接从 音频文件
        mediaCodec.configure(audioFormat, null, null, 0)
        val pcmFile = File(outPath)
        val writeChannel = FileOutputStream(pcmFile).channel
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
//                    丢掉 不用了
                    mediaExtractor.advance()
                    continue
                } else if (sampleTimeUs > endTime) {
                    break
                }
                //                获取到压缩数据
                info.size = mediaExtractor.readSampleData(buffer, 0)
                info.presentationTimeUs = sampleTimeUs
                info.flags = mediaExtractor.sampleFlags

//                下面放数据  到dsp解码
                val content = ByteArray(buffer.remaining())
                buffer[content]
                //                输出文件  方便查看
//                FileUtils.writeContent(content);
//                解码
                val inputBuffer = mediaCodec.getInputBuffer(decodeInputIndex)
                inputBuffer!!.put(content)
                mediaCodec.queueInputBuffer(
                    decodeInputIndex,
                    0,
                    info.size,
                    info.presentationTimeUs,
                    info.flags
                )
                //                释放上一帧的压缩数据
                mediaExtractor.advance()
            }
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 1000)
            while (outputBufferIndex >= 0) {
                val decodeOutputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex)
                writeChannel.write(decodeOutputBuffer) //MP3  1   pcm2
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 1000)
            }
        }
        writeChannel.close()
        mediaExtractor.release()
        mediaCodec.stop()
        mediaCodec.release()
        //        转换MP3    pcm数据转换成mp3封装格式
//
//        File wavFile = new File(Environment.getExternalStorageDirectory(),"output.mp3" );
//        new PcmToWavUtil(44100,  AudioFormat.CHANNEL_IN_STEREO,
//                2, AudioFormat.ENCODING_PCM_16BIT).pcmToWav(pcmFile.getAbsolutePath()
//                , wavFile.getAbsolutePath());
        Log.i("David", "mixAudioTrack: 转换完毕")
    }

    fun selectTrack(extractor: MediaExtractor, audio: Boolean): Int {
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