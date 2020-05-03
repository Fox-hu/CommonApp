package com.silver.fox.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.silver.fox.view.BubbleMessageTouchListener
import com.silver.fox.view.MessageBubbleView
import com.silver.fox.view.R
import kotlinx.android.synthetic.main.view_activity_bubble.*

class BubbleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_bubble)
        tv_test.setOnClickListener {

        }
        MessageBubbleView.attach(tv_test,
            object : BubbleMessageTouchListener.BubbleDisappearListener {
                override fun onDismiss() {

                }
            })
    }
}
