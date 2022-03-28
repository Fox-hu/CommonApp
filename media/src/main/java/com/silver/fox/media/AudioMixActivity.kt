package com.silver.fox.media

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.media.rtmp.encoder.PermissionUtils
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

class AudioMixActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils().verifyStoragePermissions(this, null)
        setContentView(R.layout.media_activity_audio_mix)
    }

    fun mixAudioTrack(
        videoInput: String,
        audioInput: String,
        output: String,
        videoVolume: Int,
        aacVolume: Int,
        startTimeUs: Int,
        endTimeUs: Int
    ) {
        val cacheDir = File(Environment.getExternalStorageDirectory(), "movie")
        val videoPcmFile = File(cacheDir, "video.pcm")
        decode2Pcm(videoInput, videoPcmFile.absolutePath, startTimeUs, endTimeUs)
    }

    //剪辑video的pcm音频
    @SuppressLint("WrongConstant")
    private fun decode2Pcm(
        videoInput: String,
        outPath: String,
        startTimeUs: Int,
        endTimeUs: Int
    ) {
        //解封装 mp4 分为 h264 和aac
        val mediaExtractor = MediaExtractor()
        //从封装中分离出目标为audio的源
        val index = (0..mediaExtractor.trackCount).first {
            mediaExtractor.getTrackFormat(it).getString(MediaFormat.KEY_MIME)
                ?.startsWith("audio/") == true
        }
        mediaExtractor.selectTrack(index)
        //创建解码器
        val oriAudioFormat = mediaExtractor.getTrackFormat(index)
        val mimeName = oriAudioFormat.getString(MediaFormat.KEY_MIME) ?: ""
        val decoder = MediaCodec.createDecoderByType(mimeName)

        //配置解码器
        //第一个参数代表待编/解码的数据格式
        //第二个参数代表设置的surface,用来在其上绘制解码器解码出的数据
        //第三个参数与数据加密有关
        //第四个参数 1表示编码器 0表示解码器
        decoder.configure(oriAudioFormat, null, null, 0)
        //获取音频流中的最大输入bufferSize
        val maxBufferSize = oriAudioFormat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE)
        val byteBuffer = ByteBuffer.allocateDirect(maxBufferSize)
        //开启解码流程
        decoder.start()

        val bufferInfo = MediaCodec.BufferInfo()
        val TIMEOUT_US = 2500L
        val pcmFile = File(outPath)
        val writeChannel = FileOutputStream(pcmFile).channel

        //处理解码完成后的数据
        var decodeDone = false
        var decodeInputDone = false

        while (!decodeDone) {
            // 查找解码器中,输入的索引
            var eof = false
            val dequeueInputIndex = decoder.dequeueInputBuffer(TIMEOUT_US)
            //输入队列
            if (!decodeInputDone) {
                if (dequeueInputIndex >= 0) {
                    //如果解码器中的索引有效 获取音频的pts (播放时间戳)
                    val sampleTimeUs = mediaExtractor.sampleTime
                    if (sampleTimeUs == -1L) {
                        //如果是-1 代表来到视频末尾
                        eof = true
                    } else if (sampleTimeUs < startTimeUs) {
                        //pts在选定时间戳的前面 丢弃
                        mediaExtractor.advance()
                        continue
                    } else if (sampleTimeUs > endTimeUs) {
                        eof = true
                    }

                    if (!eof) {
                        //得到一帧的数据
                        val size = mediaExtractor.readSampleData(byteBuffer, 0)
                        val flags = mediaExtractor.sampleFlags
                        bufferInfo.size = size
                        bufferInfo.flags = flags
                        bufferInfo.presentationTimeUs = sampleTimeUs
                        // 获取传送带上的托盘
                        val inputBuffer = decoder.getInputBuffer(dequeueInputIndex)
                        //将自己的箱子放到托盘上
                        inputBuffer?.put(byteBuffer)
                        decoder.queueInputBuffer(dequeueInputIndex, 0, size, sampleTimeUs, flags)
                        mediaExtractor.advance()
                    } else {
                        //如果是结尾 告诉解码器到了一帧末尾
                        decodeInputDone = true
                        decoder.queueInputBuffer(
                            dequeueInputIndex,
                            0,
                            0,
                            0,
                            MediaCodec.BUFFER_FLAG_END_OF_STREAM
                        )
                    }
                }
            }

            // 输出队列 输入的块和输出的块是一对多的关系 即1个输入buffer对应1个或多个输出
            output@ while (!decodeDone) {
                when (val outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US)) {
                    MediaCodec.INFO_TRY_AGAIN_LATER -> break@output
                    else -> {
                        if (bufferInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) break@output
                        //得到解压后的数据, 写入文件
                        writeChannel.write(decoder.getOutputBuffer(outputBufferIndex))
                        //释放这个buffer的内存
                        decoder.releaseOutputBuffer(outputBufferIndex, false)
                    }
                }
            }
        }
        //释放资源
        writeChannel.close()
        mediaExtractor.release()
        decoder.stop()
        decoder.release()
    }
}