package com.silver.fox.activity.statusbar

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.ext.setStatusBarColor
import com.silver.fox.view.R

class NotchCompatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_normal_status)
        setStatusBarColor(android.R.color.holo_blue_light)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            setStatusBarColor(android.R.color.transparent)
        }, 3000)

        Handler().postDelayed({
            setStatusBarColor(android.R.color.holo_blue_light)
        }, 6000)
    }
}