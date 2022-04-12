package com.silver.fox.media.audio.pcm

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import com.silver.fox.common.InitApp
import com.silver.fox.ext.logi
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


/**
 * 将一个mp3文件A 裁剪从startTime 到 endTime 中间的音频端
 * 生成另一个mp3文件B
 */
class PcmEditor(private val inputPath: String) {

    private val TAG = "PcmEditor"

    //相当于解压工具，从多媒体文件中解封装
    private val extractor: MediaExtractor = MediaExtractor()
    private val audioFormat: MediaFormat
    private val maxBufferSize: Int
    private val buffer: ByteBuffer
    private val mediaCodec: MediaCodec
    private val info = MediaCodec.BufferInfo()

    init {
        extractor.setDataSource(inputPath)
        //选择音频轨
        val audioTrack = getAudioTrack()
        extractor.selectTrack(audioTrack)
        //获取该音频轨的格式信息
        audioFormat = extractor.getTrackFormat(audioTrack)
        //设置buffer
        maxBufferSize =
            if (audioFormat.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) audioFormat.getInteger(
                MediaFormat.KEY_MAX_INPUT_SIZE
            ) else 100 * 1000
        buffer = ByteBuffer.allocateDirect(maxBufferSize)
        //初始化解码器
        mediaCodec = MediaCodec.createDecoderByType(audioFormat.getString(MediaFormat.KEY_MIME)!!)
        mediaCodec.configure(audioFormat, null, null, 0)
    }

    @SuppressLint("WrongConstant")
    fun clip(startTime: Int, endTime: Int, callback: (pcmFilePath: String) -> Unit) {
        if (endTime < startTime) return
        extractor.seekTo(startTime.toLong(), MediaExtractor.SEEK_TO_CLOSEST_SYNC)
        mediaCodec.start()
        val pcmFile = File("${InitApp.CONTEXT.filesDir?.parentFile?.path}", "output.pcm")
        if (pcmFile.exists()) pcmFile.delete()
        val writeChannel = FileOutputStream(pcmFile).channel

        /**
         * 解码的主要流程
         * 输入队列
         * 1、dequeueInputBuffer-> 获取decodeInputIndex
         * 2、getInputBuffer(decodeInputIndex) -> 获取目标inputBuffer,将获取的字节数组传入到inputBuffer中
         * 3、queueInputBuffer(decodeInputIndex)
         * 裁剪目标音频 需要获得 1、目标音频一帧的size 2、目标音频一帧的字节数组 3、目标音频一帧的pts
         * 这些都由MediaExtractor这解封装者提供
         *
         * 输出队列
         * 获取输出是循环中的循环
         * 1、dequeueOutputBuffer -> 获取outputBufferIndex
         * 2、getOutputBuffer(outputBufferIndex) -> 获取目标outputBuffer,从outputBuffer中获取解码后的数据，进行处理
         * 3、releaseOutputBuffer(outputBufferIndex) 释放outputBuffer
         */
        var outputBufferIndex = -1
        while (true) {
            //送入输入的bytebuffer队列
            val decodeInputIndex = mediaCodec.dequeueInputBuffer(100000)
            "dequeueInputBuffer invoke, decodeInputIndex = $decodeInputIndex".logi(TAG)
            if (decodeInputIndex >= 0) {
                val sampleTime = extractor.sampleTime
                if (sampleTime == -1L) {
                    break
                } else if (sampleTime < startTime) {
                    extractor.advance()
                    continue
                } else if (sampleTime > endTime) {
                    break
                }

                info.size = extractor.readSampleData(buffer, 0)
                info.presentationTimeUs = sampleTime
                info.flags = extractor.sampleFlags

                val content = ByteArray(buffer.remaining())
                buffer.get(content)

                val inputBuffer = mediaCodec.getInputBuffer(decodeInputIndex)
                inputBuffer?.put(content)
                mediaCodec.queueInputBuffer(
                    decodeInputIndex,
                    0,
                    info.size,
                    info.presentationTimeUs,
                    info.flags
                )
                //这里注意 释放上一帧数据
                extractor.advance()
            }

            //从输出的bytebuffer中读取处理后的结果
            outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100000)
            "dequeueOutputBuffer invoke, outputBufferIndex = $outputBufferIndex".logi(TAG)
            while (outputBufferIndex >= 0) {
                "while handle output begin , outputBufferIndex = $outputBufferIndex".logi(TAG)
                val outputBuffer = mediaCodec.getOutputBuffer(outputBufferIndex)
                writeChannel.write(outputBuffer)
                mediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                //处理一帧完毕后继续取输出buffer 更新outputBufferIndex
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(info, 100000)
                "while handle output end, outputBufferIndex = $outputBufferIndex".logi(TAG)
            }
        }
        extractor.release()
        mediaCodec.stop()
        mediaCodec.release()
        callback(pcmFile.absolutePath)
    }

    private fun getAudioTrack(): Int {
        for (i in 0..extractor.trackCount) {
            if (extractor.getTrackFormat(i).getString(MediaFormat.KEY_MIME)!!
                    .startsWith("audio/")
            ) {
                return i
            }
        }
        return -1
    }
}