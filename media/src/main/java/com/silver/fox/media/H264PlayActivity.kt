package com.silver.fox.media

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.copyAssets
import com.silver.fox.media.rtmp.encoder.PermissionUtils
import com.silver.fox.media.video.H264Player
import java.io.File

class H264PlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils().verifyStoragePermissions(this, null)
        setContentView(R.layout.media_activity_h264_player)
        checkPermission()
        initSurface()
    }

    private fun initSurface() {
        val surface = findViewById<SurfaceView>(R.id.preview)
        val holder = surface.holder
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (holder != null) {
                    val path = getTestH264Path()
                    H264Player(this@H264PlayActivity, path, holder.surface, 368, 384).play()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }
        })
    }

    private fun getTestH264Path(): String {
        copyAssets(
            assets, "sample.h264",
            File("${filesDir?.parentFile?.path}").absolutePath,true
        )
        return File("${filesDir?.parentFile?.path}", "sample.h264").absolutePath
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }
        return false
    }
}