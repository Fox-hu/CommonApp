package com.silver.fox.activity

import android.animation.LayoutTransition
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.silver.fox.view.R
import kotlinx.android.synthetic.main.view_activity_viewgroup_transition.*

class ViewGroupTransitionActivity : AppCompatActivity() {
    var index = 0
    val layoutTransition = LayoutTransition()//初始化LayoutTransition实例

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_activity_viewgroup_transition)

        //开启ViewGroup的Layout Animate
        viewGroup.layoutTransition = layoutTransition

        //控制APPEARING动画
        inCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layoutTransition.enableTransitionType(LayoutTransition.APPEARING)
            else
                layoutTransition.disableTransitionType(LayoutTransition.APPEARING)
        }
        //控制DISAPPEARING动画
        outCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layoutTransition.enableTransitionType(LayoutTransition.DISAPPEARING)
            else
                layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING)
        }
        //控制CHANGE-APPEARING动画
        inChangeCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layoutTransition.enableTransitionType(LayoutTransition.CHANGE_APPEARING)
            else
                layoutTransition.disableTransitionType(LayoutTransition.CHANGE_APPEARING)
        }
        //控制CHANGE-DISAPPEARING动画
        outChangeCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                layoutTransition.enableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)
            else
                layoutTransition.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)
        }
        //设置自定义动画，改变APPEARING动画
        customAnimateCb.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewGroup.layoutTransition = LayoutTransition().also {
                    it.setAnimator(
                        LayoutTransition.APPEARING,
                        ObjectAnimator.ofFloat(null, View.ROTATION_X, 0f, 360f)
                    )
                }
            } else {
                viewGroup.layoutTransition = layoutTransition
            }
        }
        //Add View
        btnAdd.setOnClickListener {
            val view = Button(this).apply {
                text = "Button ${index++}"
            }
            viewGroup.addView(
                view, 0, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
        //Remove View
        btnRemove.setOnClickListener {
            var childCount = viewGroup.childCount
            if (childCount > 0) {
                viewGroup.removeViewAt(0)
            }
        }

    }
}
