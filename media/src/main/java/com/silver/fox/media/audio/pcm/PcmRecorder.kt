package com.silver.fox.media.audio.pcm

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import com.silver.fox.media.EncoderConfig
import java.util.concurrent.atomic.AtomicBoolean

class PcmRecorder(private val config: EncoderConfig) {

    companion object {
        private const val TAG = "PcmRecorder"
        private val SAMPLE_RATE_ARRAY =
            intArrayOf(48000, 47250, 44100, 32000, 22050, 16000, 11025, 8000)
        private val CHANNEL_COUNT_ARRAY =
            intArrayOf(AudioFormat.CHANNEL_IN_STEREO, AudioFormat.CHANNEL_IN_MONO)
        private val AUDIO_DEPTH_ARRAY =
            intArrayOf(AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT)

        private const val MIC_AUDIO_RECORD_MSG = 0x00
        private const val MIC_INIT_AUDIO_RECORD_THREAD = 0x01
        private const val MIC_INIT_AUDIO_RECORD = 0x02
        private const val MIC_RE_INIT_AUDIO_RECORD = 0x03
        private const val MIC_START_AUDIO_RECORD = 0x04
        private const val MIC_STOP_AUDIO_RECORD = 0x05
        private const val MIC_RELEASE_AUDIO_RECORD = 0x06
        private const val MIC_DESTROY_AUDIO_RECORD_THREAD = 0x07
    }

    private var mAudioRecord: AudioRecord? = null
    private var mMICPhoneThread: HandlerThread? = null
    private var mMICPhoneHandle: MICPhoneCallHandle? = null

    private var recordBitsPerSample: Int = 0
    private var recordChannelCount: Int = 0
    private var recordSampleRate: Int = 0
    private var recordSource: Int = 0
    private var recordTimeout: Float = 0.0f


    private var mIsRecording: AtomicBoolean = AtomicBoolean(false)
    private var mAudioRecordStatus: AtomicBoolean = AtomicBoolean(false)

    init {
        mMICPhoneThread = HandlerThread(TAG)
        mMICPhoneThread?.start()
        mMICPhoneHandle = mMICPhoneThread?.looper?.let { MICPhoneCallHandle(it) }

        recordSampleRate = config.sampleRateInHz
        recordChannelCount = config.channelCount
        recordBitsPerSample = config.audioDepth
        recordSource = MediaRecorder.AudioSource.MIC
        recordTimeout = 0.2f
    }

    fun startRecord() {
        mMICPhoneHandle?.apply {
            sendEmptyMessage(MIC_STOP_AUDIO_RECORD)
            sendEmptyMessage(MIC_RELEASE_AUDIO_RECORD)
            sendEmptyMessage(MIC_DESTROY_AUDIO_RECORD_THREAD)
        }
    }

    private inner class MICPhoneCallHandle(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MIC_INIT_AUDIO_RECORD_THREAD -> micInitAudioRecordThread()
                MIC_INIT_AUDIO_RECORD -> micInitAudioRecord()
                MIC_RE_INIT_AUDIO_RECORD -> micReInitAudioRecord(msg.arg1)
                MIC_START_AUDIO_RECORD -> micStartAudioRecord()
                MIC_STOP_AUDIO_RECORD -> micStopAudioRecord()
                MIC_RELEASE_AUDIO_RECORD -> micReleaseAudioRecord()
                MIC_DESTROY_AUDIO_RECORD_THREAD -> micDestroyAudioRecordThread()
            }
        }
    }

    private fun micDestroyAudioRecordThread() {
    }

    private fun micReleaseAudioRecord() {
    }

    private fun micStopAudioRecord() {
        val msg = Message.obtain()
        msg.what = MIC_AUDIO_RECORD_MSG
        try {
            if (mAudioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                msg.obj = MICAudioRecordStatus(
                    MIC_STOP_AUDIO_RECORD,
                    -1,
                    "cur audioRecord state != AudioRecord.STATE_INITIALIZED"
                )
                Log.w(TAG, (msg.obj as MICAudioRecordStatus).msg)
                return
            }
            mIsRecording.set(false)
            mAudioRecord?.stop()
            msg.obj = MICAudioRecordStatus(
                MIC_STOP_AUDIO_RECORD,
                0,
                "audioRecord stop ok!"
            )
            Log.i(TAG, (msg.obj as MICAudioRecordStatus).msg)
        } catch (e: IllegalStateException) {
            msg.obj = MICAudioRecordStatus(
                MIC_STOP_AUDIO_RECORD,
                -99,
                "audioRecord stop exception: ${e.message}"
            )
            Log.e(TAG, (msg.obj as MICAudioRecordStatus).msg)
        } finally {
            mMICPhoneHandle?.sendMessage(msg)
        }
    }

    private fun micStartAudioRecord() {
    }

    private fun micReInitAudioRecord(arg1: Int) {
    }

    private fun micInitAudioRecord() {
    }

    private fun micInitAudioRecordThread() {
    }

    data class MICAudioRecordStatus(val type: Int, val errorCode: Int, val msg: String)
}