package com.silver.fox.media

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.showShortToast
import com.silver.fox.media.video.CameraSurfaceView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraCaptureActivity : AppCompatActivity() {

    var cameraSurfaceView: CameraSurfaceView? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity_camera_capture)
        cameraSurfaceView = findViewById(R.id.media_camera_surface)
        checkPermission()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            cameraSurfaceView?.startPreview()
        }
    }

    //图片有问题 暂时不知道什么原因
    fun capture(view: View?) {
        cameraSurfaceView?.startCapture { data, size ->
            val pictureFile =
                File("${filesDir?.parentFile?.path}", "IMG_${System.currentTimeMillis()}.jpg")
            if (!pictureFile.exists()) {
                try {
                    pictureFile.createNewFile()
                    val filecon = FileOutputStream(pictureFile)
                    //ImageFormat.NV21 and ImageFormat.YUY2 for now
                    val image = YuvImage(
                        data,
                        ImageFormat.NV21,
                        size!!.height,
                        size.width,
                        null
                    ) //将NV21 data保存成YuvImage
                    //图像压缩
                    image.compressToJpeg(
                        Rect(0, 0, image.width, image.height),
                        100, filecon
                    ) // 将NV21格式图片，以质量70压缩成Jpeg，并得到JPEG数据流
                    "生成图片成功，${pictureFile.name}".showShortToast()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission() {
        if (checkSelfPermission(
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA
                ), 100
            )
        }
    }
}