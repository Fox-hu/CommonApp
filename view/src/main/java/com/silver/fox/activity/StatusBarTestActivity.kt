package com.silver.fox.activity

import com.silver.fox.activity.statusbar.FullScreenActivity
import com.silver.fox.activity.statusbar.NormalStatusActivity
import com.silver.fox.activity.statusbar.NotchCompatActivity
import com.silver.fox.base.BaseListActivity

class StatusBarTestActivity : BaseListActivity() {

    override fun initItem() {
        activityMap["NormalStatusActivity"] = NormalStatusActivity::class.java
        activityMap["FullScreenActivity"] = FullScreenActivity::class.java
        activityMap["NotchCompatActivity"] = NotchCompatActivity::class.java
    }
}
