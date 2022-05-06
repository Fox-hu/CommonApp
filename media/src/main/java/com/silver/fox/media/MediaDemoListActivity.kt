package com.silver.fox.media

import com.silver.fox.base.BaseListActivity

class MediaDemoListActivity : BaseListActivity() {
    override fun initItem() {
        activityMap["RtmpDemoActivity"] = RtmpDemoActivity::class.java
        activityMap["ScreenToH264Activity"] = ScreenToH264Activity::class.java
        activityMap["CameraCaptureActivity"] = CameraCaptureActivity::class.java
        activityMap["AudioFlipActivity"] = AudioFlipActivity::class.java
        activityMap["AudioVideoMixActivity"] = AudioVideoMixActivity::class.java
        activityMap["VideoMixActivity"] = VideoMixActivity::class.java
    }
}