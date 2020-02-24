package com.silver.fox

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.view.R
import kotlinx.android.synthetic.main.view_activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_test)
        step_view.stepMax = 4000
        val animator = ObjectAnimator.ofInt(0, 3000)
        animator.duration = 1000
        animator.addUpdateListener {
            step_view.currentStep = it.animatedValue as Int
        }
        animator.start()
    }
}
