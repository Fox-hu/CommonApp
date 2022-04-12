package com.silver.fox.media

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.toFile
import com.silver.fox.media.video.EncoderParam
import com.silver.fox.media.video.H264Player
import com.silver.fox.media.video.ScreenToH264Encoder
import java.io.File

/**
 * 录制屏幕操作 生成h264文件
 * 并播放该h264文件
 */
class ScreenToH264Activity : AppCompatActivity() {

    private var path: String = ""
    private var screenToH264Encoder: ScreenToH264Encoder? = null
    private var isEncoding = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity_screen_to_h264)
        path = File("${filesDir?.parentFile?.path}", "screen_record.h264").absolutePath
        findViewById<Button>(R.id.media_screen_action).setOnClickListener {
            handleScreen(it)
        }
        findViewById<Button>(R.id.media_h264_player_btn).setOnClickListener {
            val holder = findViewById<SurfaceView>(R.id.media_h264_play_surface).holder
            H264Player(this, path, holder.surface, 1080, 1920).play()
        }
        requestScreenCapturePermission()
    }

    val handleScreen: (View) -> Unit = {
        if (isEncoding) {
            (it as Button).text = "开始录屏"
            screenToH264Encoder?.stop()
        } else {
            if (File(path).exists()) File(path).delete()
            (it as Button).text = "停止录屏"
            screenToH264Encoder?.start()
        }
        isEncoding = !isEncoding
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100 && data != null) {
            startPlaybackCaptureService(data)
        }
    }

    private fun startPlaybackCaptureService(intent: Intent) {
        val serviceIntent = Intent(this, ScreenCaptureService::class.java)
        serviceIntent.putExtra(ScreenCaptureService.KEY_RESULT_CODE, Activity.RESULT_OK)
        serviceIntent.putExtra(ScreenCaptureService.KEY_SCREEN_INTENT, intent)
        serviceIntent.putExtra(ScreenCaptureService.KEY_NOTIFICATION_TITLE, "录屏测试")
        serviceIntent.putExtra(ScreenCaptureService.KEY_NOTIFICATION_DESC, "录屏测试")
        //为了兼容主站升级target版本到android 11版本，对应sdk 30
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                startForegroundService(serviceIntent)
            } catch (e: Exception) {

            }
        } else {
            startService(serviceIntent)
        }
        bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val serviceBridge = service as ScreenCaptureService.ServiceBridge
            serviceBridge.getMediaProject()
                ?.let {
                    screenToH264Encoder =
                        ScreenToH264Encoder(EncoderParam(1080, 1920), it) { byteArray ->
                            byteArray.toFile(path)
                        }
                }
        }

        override fun onServiceDisconnected(name: ComponentName?) {}
    }

    private fun requestScreenCapturePermission() {
        val mpManager =
            applicationContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val requestPermissionIntent = mpManager.createScreenCaptureIntent()
        startActivityForResult(requestPermissionIntent, 100)
    }
}