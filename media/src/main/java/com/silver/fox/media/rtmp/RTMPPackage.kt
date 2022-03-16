package com.silver.fox.media.rtmp

class RTMPPackage(var buffer: ByteArray, var tms: Long) {
    //    视频包 音频包
    var type = 0

    companion object {
        const val RTMP_PACKET_TYPE_AUDIO_DATA = 2
        const val RTMP_PACKET_TYPE_AUDIO_HEAD = 1
        const val RTMP_PACKET_TYPE_VIDEO = 0
    }
}