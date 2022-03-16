package com.silver.fox.media

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.media.MediaCodec
import android.media.MediaFormat
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Base64
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.media.rtmp.CameraLive
import com.silver.fox.media.rtmp.RTMPPackage
import com.silver.fox.media.rtmp.encoder.AACEncoder
import com.silver.fox.media.rtmp.encoder.AVCEncoder
import com.silver.fox.media.rtmp.encoder.PermissionUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

class MediaDemoActivity : AppCompatActivity(), View.OnClickListener,
    SurfaceTextureListener {
    private lateinit var camera: Camera
    private var startlive: Button? = null
    private var stoplive: Button? = null
    private var tureview: TextureView? = null

    private var aacEncoder: AACEncoder? = null
    private var avcEncoder: AVCEncoder? = null
    private var cameraLive: CameraLive? = null

    private var startVideoTime: Long = 0
    private var startAudioTime: Long = 0

    //private String url = "rtmp://sendtc3.douyu.com/live/4375298rsIyZjaUt?wsSecret=e14d3a1a21d0e1793f8f0dd9c0e8ae0f&wsTime=606afdd9&wsSeek=off&wm=0&tw=0&roirecognition=0&record=flv&origin=tct";
    private val url =
        "rtmp://live-push.bilivideo.com/live-bvc/?streamname=live_13031751_64675177&key=da4bf1157e4db938bc1886664efe07a3&schedule=rtmp&pflag=1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils().verifyStoragePermissions(this, null)
        setContentView(R.layout.media_activity_media_demo)
        initView()
        cameraLive = CameraLive()
        cameraLive?.startLive(url)
    }

    private fun initView() {
        startlive = findViewById<Button>(R.id.startlive).apply {
            setOnClickListener(this@MediaDemoActivity)
        }
        stoplive = findViewById<Button>(R.id.stoplive).apply {
            setOnClickListener(this@MediaDemoActivity)
        }
        tureview = findViewById<TextureView>(R.id.tureview).apply {
            surfaceTextureListener = this@MediaDemoActivity
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.startlive -> startRecoding()
            R.id.stoplive -> stopRecoding()
        }
    }

    private fun tt(content: ByteArray) {
        try {
            val mp3SoundByteArray = Base64.decode(content, Base64.DEFAULT) // 将字符串转换为byte数组
            val tempMp3 = File.createTempFile("badao", ".mp3")
            tempMp3.deleteOnExit()
            val fos = FileOutputStream(tempMp3)
            fos.write(mp3SoundByteArray)
            fos.close()
            val mediaPlayer = MediaPlayer()
            val fis = FileInputStream(tempMp3)
            mediaPlayer.setDataSource(fis.fd)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (ex: IOException) {
            val s = ex.toString()
            ex.printStackTrace()
        }
    }


    private fun stopRecoding() {
        aacEncoder?.stop()
        avcEncoder?.stop()
        cameraLive?.disconnect()
    }

    private fun startRecoding() {
        initVideo()
        initAudio()
    }

    private fun initVideo() {
        avcEncoder = AVCEncoder(camera)
        avcEncoder?.setVideoEnncoderListener(object : AVCEncoder.VideoEnncoderListener {
            override fun getVideoBuffer(
                mediaCodec: MediaCodec?,
                encoderStatus: Int,
                byteBuffer: ByteBuffer?,
                bufferInfo: MediaCodec.BufferInfo?,
                data: ByteArray
            ) {
                if (startVideoTime == 0L) {
                    startVideoTime = bufferInfo?.presentationTimeUs?.div(1000) ?: 0
                }
                val rtmpPackage =
                    RTMPPackage(
                        data,
                        (bufferInfo?.presentationTimeUs?.div(1000) ?: 0) - startVideoTime
                    )
                rtmpPackage.type = RTMPPackage.RTMP_PACKET_TYPE_VIDEO
                cameraLive?.addPackage(rtmpPackage)
            }

            override fun getOutputFormat(format: MediaFormat?) {}
        })
        try {
            camera?.let { ca ->
                avcEncoder?.apply {
                    prepare(
                        ca.parameters.previewSize.width,
                        ca.parameters.previewSize.height
                    )
                    start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initAudio() {
        aacEncoder = AACEncoder()
        val rtmpPackage = RTMPPackage(byteArrayOf(0x12, 0x08), 0).apply {
            type = RTMPPackage.RTMP_PACKET_TYPE_AUDIO_HEAD
        }
        cameraLive?.addPackage(rtmpPackage)
        aacEncoder?.audioEnncoderListener = object : AACEncoder.AudioEnncoderListener {
            override fun getAudioBuffer(
                mediaCodec: MediaCodec?,
                status: Int,
                byteBuffer: ByteBuffer?,
                bufferInfo: MediaCodec.BufferInfo?,
                data: ByteArray?
            ) {
                data?.apply {
                    if (startAudioTime == 0L) {
                        startAudioTime = bufferInfo?.presentationTimeUs?.div(1000) ?: 0
                    }
                    val tms = (bufferInfo?.presentationTimeUs ?: 0) / 1000 - startAudioTime
                    val tempPackage = RTMPPackage(data, tms).apply {
                        type = RTMPPackage.RTMP_PACKET_TYPE_AUDIO_DATA
                    }
                    cameraLive?.addPackage(tempPackage)

                }
            }

            override fun getOutputFormat(format: MediaFormat?) {}
        }
        try {
            aacEncoder?.prepare()
            aacEncoder?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        try {
            camera.stopPreview()
            camera = Camera.open().apply {
                setPreviewTexture(surface)
                setDisplayOrientation(90)
                startPreview()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        camera.stopPreview()
        camera.release()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
}