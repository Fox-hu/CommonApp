package com.silver.fox.activity

import com.silver.fox.activity.statusbar.FullScreenActivity
import com.silver.fox.activity.statusbar.NormalStatusActivity

class StatusBarTestActivity : BaseListActivity() {

    override fun initItem() {
        activityMap["NormalStatusActivity"] = NormalStatusActivity::class.java
        activityMap["FullScreenActivity"] = FullScreenActivity::class.java
    }
}
