package com.silver.fox.media.rtmp.encoder

import android.media.MediaCodec
import android.media.MediaFormat
import android.media.MediaMuxer
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer

class Mp4Muxer(outPath: String?) {
    private val TAG = javaClass.simpleName
    var mMuxer: MediaMuxer? = null
    var mVideoTrackIndex = -1
    var mAudioTrackIndex = -1

    fun addVideoTrack(mediaFormat: MediaFormat?) {
        if (mVideoTrackIndex != -1) throw RuntimeException("already add video tracks")
        mVideoTrackIndex = mMuxer!!.addTrack(mediaFormat!!)
    }

    fun addAudioTrack(mediaFormat: MediaFormat?) {
        if (mAudioTrackIndex != -1) throw RuntimeException("already add audio tracks")
        mAudioTrackIndex = mMuxer!!.addTrack(mediaFormat!!)
    }

    fun start() {
        mMuxer!!.start()
    }

    @Synchronized
    fun writeVideoData(outputBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (mVideoTrackIndex == -1) {
            Log.i(TAG, String.format("pumpStream [%s] but muxer is not start.ignore..", "video"))
            return
        }
        writeData(outputBuffer, bufferInfo, mVideoTrackIndex)
    }

    @Synchronized
    fun writeAudioData(outputBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
        if (mAudioTrackIndex == -1) {
            Log.i(TAG, String.format("pumpStream [%s] but muxer is not start.ignore..", "audio"))
            return
        }
        writeData(outputBuffer, bufferInfo, mAudioTrackIndex)
    }

    fun writeData(outputBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo, track: Int) {
        if (mMuxer == null) {
            return
        }
        if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
            // The codec config data was pulled out and fed to the muxer when we got
            // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
            bufferInfo.size = 0
        } else if (bufferInfo.size != 0) {
            outputBuffer.position(bufferInfo.offset)
            outputBuffer.limit(bufferInfo.offset + bufferInfo.size)
            mMuxer!!.writeSampleData(track, outputBuffer, bufferInfo)
            if (VERBOSE) Log.d(
                TAG,
                String.format(
                    "send [%d] [" + bufferInfo.size + "] with timestamp:[%d] to muxer",
                    track,
                    bufferInfo.presentationTimeUs
                )
            )
            if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                if (VERBOSE) Log.i(TAG, "BUFFER_FLAG_END_OF_STREAM received")
            }
        }
    }

    @Synchronized
    fun stop() {
        if (mMuxer != null) {
            if (mVideoTrackIndex != -1 || mAudioTrackIndex != -1) { //mAudioTrackIndex != -1 &&
                if (VERBOSE) Log.i(TAG, String.format("muxer is started. now it will be stoped."))
                try {
                    mMuxer!!.stop()
                    mMuxer!!.release()
                    mMuxer = null
                } catch (ex: IllegalStateException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    companion object {
        const val VERBOSE = true
    }

    init {
        try {
            mMuxer = MediaMuxer(outPath!!, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}