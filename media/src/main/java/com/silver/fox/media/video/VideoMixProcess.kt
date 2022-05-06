package com.silver.fox.media.video

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.media.MediaMuxer
import java.nio.ByteBuffer

class VideoMixProcess {

    companion object {

        /**
         * 合并两个视频 将一个视频合并到另一个视频末尾
         * 可以想到 过程就是 从视频1中获取视频轨和音频轨 写入合成器的视频轨和音频轨
         * 再从视频2中获取视频轨和音频轨 pts+视频1的总长度 添加到合成器的视频轨和音频轨后
         */
        @SuppressLint("WrongConstant")
        fun appendVideo(inputPath1: String, inputPath2: String, outputPath: String) {
            val mediaMuxer = MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            val videoExtractor1 = MediaExtractor().apply { setDataSource(inputPath1) }
            val videoExtractor2 = MediaExtractor().apply { setDataSource(inputPath2) }

            var videoTrackIndex = -1
            var audioTrackIndex = -1
            var file1Duration = 0L

            var sourceVideoTrack1 = -1
            var sourceAudioTrack1 = -1

            for (i in 0 until videoExtractor1.trackCount) {
                val format = videoExtractor1.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                file1Duration = format.getLong(MediaFormat.KEY_DURATION)
                mime ?: return
                if (mime.startsWith("video/")) {
                    sourceVideoTrack1 = i
                    //将第一个视频的视频轨作为合成器的视频轨的参数
                    videoTrackIndex = mediaMuxer.addTrack(format)
                } else if (mime.startsWith("audio/")) {
                    sourceAudioTrack1 = i
                    //将第一个视频的音频轨作为合成器的音频轨的参数
                    audioTrackIndex = mediaMuxer.addTrack(format)
                }
            }

            var sourceVideoTrack2 = -1
            var sourceAudioTrack2 = -1

            for (i in 0 until videoExtractor2.trackCount) {
                val format = videoExtractor2.getTrackFormat(i)
                val mime = format.getString(MediaFormat.KEY_MIME)
                mime ?: return
                if (mime.startsWith("video/")) {
                    sourceVideoTrack2 = i
                } else if (mime.startsWith("audio/")) {
                    sourceAudioTrack2 = i
                }
            }

            mediaMuxer.start()
            //1、抽取视频1的视频轨 写入合成器视频轨数据
            videoExtractor1.selectTrack(sourceVideoTrack1)
            extractMedia(videoExtractor1, mediaMuxer, videoTrackIndex)

            //2、抽取视频1的音频轨 写入合成器音频轨数据
            videoExtractor1.unselectTrack(sourceVideoTrack1)
            videoExtractor1.selectTrack(sourceAudioTrack1)
            extractMedia(videoExtractor1, mediaMuxer, audioTrackIndex)

            //3、抽取视频2的视频轨 写入合成器视频轨数据的最后 即pts需要加上视频1的总视频时长
            videoExtractor2.selectTrack(sourceVideoTrack2)
            extractMedia(videoExtractor2, mediaMuxer, videoTrackIndex, file1Duration)

            //4、抽取视频2的音频轨 写入合成器音频轨数据的最后 即pts需要加上视频1的总视频时长
            videoExtractor2.unselectTrack(sourceVideoTrack2)
            videoExtractor2.selectTrack(sourceAudioTrack2)
            extractMedia(videoExtractor2, mediaMuxer, audioTrackIndex, file1Duration)

            videoExtractor1.release()
            videoExtractor2.release()
            mediaMuxer.stop()
            mediaMuxer.release()
        }

        /**
         * 从extractor中抽取音频或视频数据，写入到mediaMuxer的muxTrackIndex中
         */
        @SuppressLint("WrongConstant")
        private fun extractMedia(
            extractor: MediaExtractor,
            mediaMuxer: MediaMuxer,
            muxTrackIndex: Int,
            ptsOffset: Long = 0L
        ) {
            val info = MediaCodec.BufferInfo()
            val buffer = ByteBuffer.allocate(500 * 1024)
            info.presentationTimeUs = 0
            var sampleSize = 0
            while (extractor.readSampleData(buffer, 0).also { sampleSize = it } > 0) {
                info.offset = 0
                info.size = sampleSize
                info.flags = extractor.sampleFlags
                info.presentationTimeUs = extractor.sampleTime + ptsOffset

                mediaMuxer.writeSampleData(muxTrackIndex, buffer, info)
                extractor.advance()
            }
        }
    }
}