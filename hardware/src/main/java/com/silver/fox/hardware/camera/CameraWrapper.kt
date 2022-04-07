package com.silver.fox.hardware.camera

import android.hardware.Camera


class CameraWrapper {
    private var camera: Camera? = null

    /**
     * @param cameraInfo Camera.CameraInfo.CAMERA_FACING_BACK/CAMERA_FACING_FRONT
     *
     */
    fun initCamera(cameraInfo: Int, degrees: Int) {
        camera = Camera.open(cameraInfo).apply {
            setDisplayOrientation(degrees)
        }
    }
}