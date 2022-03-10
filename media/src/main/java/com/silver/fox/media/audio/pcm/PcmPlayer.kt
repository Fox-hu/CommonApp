package com.silver.fox.media.audio.pcm

import android.media.AudioTrack
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message

class PcmPlayer {

    companion object{
        private const val TAG = "PcmPlayer"
        private const val AUDIO_TRACK_PLAY_MSG = 0x01
        private const val AUDIO_TRACK_PLAY_INIT = 0x02
        private const val AUDIO_TRACK_PLAY_RE_INIT = 0x03
        private const val AUDIO_TRACK_PLAY_START = 0x04
        private const val AUDIO_TRACK_PLAY_RE_START = 0x05
        private const val AUDIO_TRACK_PLAY_STOP = 0x06
        private const val AUDIO_TRACK_PLAY_RELEASE = 0x07
    }

    private var mAudioTrack: AudioTrack? = null
    private var mAudioPlayThread: HandlerThread? = null

    init {

    }

    private inner class AudioPlayCallHandle(looper: Looper): Handler(looper) {
        override fun handleMessage(msg: Message) {
            when(msg.what) {
                AUDIO_TRACK_PLAY_INIT -> aTAudioPlayInit()
                AUDIO_TRACK_PLAY_START -> aTAudioPlayStart()
                AUDIO_TRACK_PLAY_STOP -> aTAudioPlayStop()
                AUDIO_TRACK_PLAY_RELEASE -> aTAudioPlayRelease()
            }
        }

    }

    private fun aTAudioPlayRelease() {
    }

    private fun aTAudioPlayStop() {
    }

    private fun aTAudioPlayStart() {
    }

    private fun aTAudioPlayInit() {
    }
}