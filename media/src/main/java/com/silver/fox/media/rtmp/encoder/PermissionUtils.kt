package com.silver.fox.media.rtmp.encoder

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtils {
    interface PermissionCallBack {
        fun success()
    }

    //权限声明
    private val REQUEST_EXTERNAL_STORAGE = 36464
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    //权限申请
    fun verifyStoragePermissions(activity: Activity?, permissionCallBack: PermissionCallBack?) {
        for (i in PERMISSIONS_STORAGE.indices) {
            val permission = ActivityCompat.checkSelfPermission(activity!!, PERMISSIONS_STORAGE[i])
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
                return
            }
        }
        permissionCallBack?.success()
    }
}