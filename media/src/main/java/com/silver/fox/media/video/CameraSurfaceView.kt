package com.silver.fox.media.video

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.RequiresApi
import java.io.IOException

class CameraSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback, Camera.PreviewCallback {
    private var mCamera: Camera? = null
    private var size: Camera.Size? = null
    var buffer: ByteArray? = null
    var action: ((ByteArray, Camera.Size?) -> Unit)? = null

    @Volatile
    private var isCapture = false

    init {
        holder.addCallback(this)
    }

    fun startCapture(cb: ((ByteArray, Camera.Size?) -> Unit)?) {
        action = cb
        isCapture = !isCapture
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            startPreview()
        }
    }

    fun startPreview() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK).apply {
            size = parameters.previewSize
            try {
                setPreviewDisplay(holder)
                //预览图像旋转90度 如果不设置 摄像头默认是横屏展示的 注意 这里改的只是预览图像的角度 输出数据仍然是 横屏的图像
                setDisplayOrientation(90)
//              int bitsPerPixel = ImageFormat.getBitsPerPixel(ImageFormat.NV21);
//              buffer = new byte[size.width*size.height * bitsPerPixel /8]; 3/2的来源
                buffer = ByteArray(size!!.width * size!!.height * 3 / 2)
                //在onPreviewFrame中调用addCallbackBuffer(data)
                //就可以一直复用原来开辟的那个内存空间了,视频数据data永远都只会保持在一个地址中,只是其中的内容在不断的变化
                addCallbackBuffer(buffer)
                setPreviewCallbackWithBuffer(this@CameraSurfaceView)
                startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

//setPreviewCallbackWithBuffer 在启动预览后，需要手动调用Camera.addCallbackBuffer(data)
//触发回调，byte[]数据需要根据一帧画面的尺寸提前创建传入
//例如NV21格式，size = width * height * 3 / 2;
    override fun onPreviewFrame(data: ByteArray, camera: Camera?) {
        if (isCapture) {
            isCapture = false

            //如果是这句 则data的数据是横屏的
            action?.let { it(data, size) }

            //这句是将横屏的数据旋转90，生成竖屏的照片
//            val rotated = portraitData2Raw(data)
//            action?.let { it(rotated, size) }
        }
        //data数据就是采集的画面数据 不能省 否则不能用
        //回收缓存，下次仍然会使用，所以不需要再开辟新的缓存，达到优化的目的
        mCamera?.addCallbackBuffer(data)
    }

    private fun portraitData2Raw(data: ByteArray): ByteArray{
        val width = size!!.width
        val height = size!!.height
        //        旋转y
        val y_len = width * height
        //u   y/4   v  y/4
        val uvHeight = height / 2
        var k = 0
        for (j in 0 until width) {
            for (i in height - 1 downTo 0) {
//                存值  k++  0          取值  width * i + j
                buffer!![k++] = data[width * i + j]
            }
        }
        //        旋转uv
        for (j in 0 until width step 2){
            for (i in uvHeight - 1 downTo 0) {
                buffer!![k++] = data[y_len + width * i + j]
                buffer!![k++] = data[y_len + width * i + j + 1]
            }
        }
        return buffer!!
    }
}