package com.silver.fox.media

import com.silver.fox.base.BaseListActivity

class MediaDemoListActivity : BaseListActivity() {
    override fun initItem() {
        activityMap["RtmpDemoActivity"] = RtmpDemoActivity::class.java
        activityMap["H264PlayActivity"] = H264PlayActivity::class.java
    }
}