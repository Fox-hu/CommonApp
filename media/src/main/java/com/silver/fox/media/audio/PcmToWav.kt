package com.silver.fox.media.audio

import android.media.AudioFormat
import android.media.AudioRecord
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by huangwei on 2018/2/9.
 */
class PcmToWav(
    private var sampleRate: Int = 8000,
    private var channelConfig: Int = AudioFormat.CHANNEL_IN_STEREO,
    private var channelCount: Int = 2,
    private var encoding: Int = AudioFormat.ENCODING_PCM_16BIT
) {

    private val bufferSize:Int = AudioRecord.getMinBufferSize(sampleRate, channelConfig, encoding)

    /**
     * pcm文件转wav文件
     *
     * @param inFilename  源文件路径
     * @param outFilename 目标文件路径
     */
    fun pcmToWav(inFilename: String?, outFilename: String?) {
        val input: FileInputStream
        val out: FileOutputStream
        val totalAudioLen: Long
        val totalDataLen: Long
        val longSampleRate = sampleRate.toLong()
        val channels = channelCount
        val byteRate = (16 * sampleRate * channels / 8).toLong()
        val data = ByteArray(bufferSize)
        try {
            input = FileInputStream(inFilename)
            out = FileOutputStream(outFilename)
            totalAudioLen = input.channel.size()
            totalDataLen = totalAudioLen + 36
            writeWaveFileHeader(
                out, totalAudioLen, totalDataLen,
                longSampleRate, channels, byteRate
            )
            while (input.read(data) != -1) {
                out.write(data)
            }
            input.close()
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 加入wav文件头
     */
    private fun writeWaveFileHeader(
        out: FileOutputStream, totalAudioLen: Long,
        totalDataLen: Long, longSampleRate: Long, channels: Int, byteRate: Long
    ) {
        val header = ByteArray(44)
        header[0] = 'R'.toByte() // RIFF/WAVE header
        header[1] = 'I'.toByte()
        header[2] = 'F'.toByte()
        header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = (totalDataLen shr 8 and 0xff).toByte()
        header[6] = (totalDataLen shr 16 and 0xff).toByte()
        header[7] = (totalDataLen shr 24 and 0xff).toByte()
        header[8] = 'W'.toByte() //WAVE
        header[9] = 'A'.toByte()
        header[10] = 'V'.toByte()
        header[11] = 'E'.toByte()
        header[12] = 'f'.toByte() // 'fmt ' chunk
        header[13] = 'm'.toByte()
        header[14] = 't'.toByte()
        header[15] = ' '.toByte()
        header[16] = 16 // 4 bytes: size of 'fmt ' chunk
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1 // format = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = (longSampleRate shr 8 and 0xff).toByte()
        header[26] = (longSampleRate shr 16 and 0xff).toByte()
        header[27] = (longSampleRate shr 24 and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = (byteRate shr 8 and 0xff).toByte()
        header[30] = (byteRate shr 16 and 0xff).toByte()
        header[31] = (byteRate shr 24 and 0xff).toByte()
        header[32] = (2 * 16 / 8).toByte() // block align
        header[33] = 0
        header[34] = 16 // bits per sample
        header[35] = 0
        header[36] = 'd'.toByte() //data
        header[37] = 'a'.toByte()
        header[38] = 't'.toByte()
        header[39] = 'a'.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = (totalAudioLen shr 8 and 0xff).toByte()
        header[42] = (totalAudioLen shr 16 and 0xff).toByte()
        header[43] = (totalAudioLen shr 24 and 0xff).toByte()
        out.write(header, 0, 44)
    }
}