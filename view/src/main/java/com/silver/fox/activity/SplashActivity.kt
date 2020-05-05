package com.silver.fox.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.silver.fox.view.R
import kotlinx.android.synthetic.main.view_activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_splash)
        splash_view.finishListener = {
            tv.visibility = View.VISIBLE
        }
        Handler().postDelayed({
            splash_view.disappear()
        }, 3000)
    }
}

