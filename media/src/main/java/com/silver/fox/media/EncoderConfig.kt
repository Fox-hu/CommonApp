package com.silver.fox.media


class EncoderConfig {
    var mimeType: String = ""
    var width: Int = -1
    var height: Int = -1
    var frameRate: Int = -1
    var videoBitRate: Int = -1
    var iFrameInterval: Int = -1

    var sampleRateInHz: Int = -1
    var channelCount: Int = -1
    var audioBitRate: Int = -1
    var audioDepth: Int = -1

    // 推流时音视频编码器的信息
    val videoEncoderInfo = VideoEncoderInfo()
    val audioEncoderInfo = AudioEncoderInfo()

    class VideoEncoderInfo {
        var codecName = ""
        var codecType = "avc"
        var codecProfile = ""
        var codecLevel = ""
        var bFrameEnable = false

        override fun toString(): String {
            return "VideoEncoderInfo(codecName='$codecName', codecType='$codecType', codecProfile='$codecProfile', codecLevel='$codecLevel', bFrameEnable=$bFrameEnable)"
        }
    }

    class AudioEncoderInfo {
        var codecType = "aac"
        var codecName = ""
        var codecProfile = ""

        override fun toString(): String {
            return "AudioEncoderInfo(codecType='$codecType', codecName='$codecName', codecProfile='$codecProfile')"
        }
    }
}
