package com.silver.fox.media.video

import android.content.Context
import android.media.MediaCodec
import android.media.MediaFormat
import android.view.Surface
import com.silver.fox.ext.getBytes
import java.io.File

class H264Player(
    val context: Context,
    val path: String,
    val surface: Surface,
    val width: Int,
    val height: Int
) : Runnable {
    private val mediaCodec: MediaCodec = MediaCodec.createDecoderByType("video/avc")

    init {
        MediaFormat.createVideoFormat("video/avc", width, height).apply {
            setInteger(MediaFormat.KEY_FRAME_RATE, 15)
            //0代表解码器 1代表编码器
            mediaCodec.configure(this, surface, null, 0)
        }
    }

    fun play() {
        mediaCodec.start()
        Thread(this).start()
    }

    override fun run() {
        decodeH264()
    }

    private fun decodeH264() {
        val bytes = File(path).getBytes() ?: return
        //内部队列 不是没一个buffer都可以使用 被弃用
        val inputBuffers = mediaCodec.inputBuffers
        var startIndex = 0
        var totalSize = bytes.size

        while (true) {
            //1、向mediaCodec塞入数据
            if (totalSize == 0 || startIndex >= totalSize) break
            //开始遍历整个文件，因为解析过程是每一帧每一帧解析，h264帧间距用 00 00 00 01标识
            //startIndex+2 是因为要跳过pps和sps这两帧
            val nextFrameStart = findByFrame(bytes, startIndex + 2, totalSize)
            if (nextFrameStart == -1) break

            val info = MediaCodec.BufferInfo()
            //查询哪个输入队列的buffer能够使用
            val inIndex = mediaCodec.dequeueInputBuffer(10000)
            if (inIndex >= 0) {
                //找到了
                val byteBuffers = inputBuffers[inIndex]
                //清空原有的buffer
                byteBuffers.clear()
                //将这一帧的数据放入到这个buffer中去
                byteBuffers.put(bytes, startIndex, nextFrameStart - startIndex)
                mediaCodec.queueInputBuffer(inIndex, 0, nextFrameStart - startIndex, 0, 0)
                //标记下一帧的开始index
                startIndex = nextFrameStart
            } else {
                continue
            }

            //2、处理解码后的数据
            //取出解码后的数据包
            val outIndex = mediaCodec.dequeueOutputBuffer(info, 10000)
            if (outIndex >= 0) {
                //如果解码后的数据包有效，则渲染到传入的surface中
                mediaCodec.releaseOutputBuffer(outIndex, true)
            }
        }
    }

    private fun findByFrame(bytes: ByteArray, start: Int, totalSize: Int): Int {
        for (i in start until totalSize - 4) {
            if (bytes[i] == 0x00.toByte() && bytes[i + 1] == 0x00.toByte() && bytes[i + 2] == 0x00.toByte() && bytes[i + 3] == 0x01.toByte())
                return i
        }
        return -1
    }
}