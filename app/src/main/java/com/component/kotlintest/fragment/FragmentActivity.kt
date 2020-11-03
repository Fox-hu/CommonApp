package com.component.kotlintest.fragment

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.component.kotlintest.R

/**
 * @Author fox.hu
 * @Date 2020/11/3 16:19
 */
class FragmentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val manager = supportFragmentManager
        var fragment1 = manager.findFragmentByTag(Fragment1::javaClass.name)
        var fragment2 = manager.findFragmentByTag(Fragment2::javaClass.name)
        var fragment3 = manager.findFragmentByTag(Fragment3::javaClass.name)

        findViewById<Button>(R.id.go_fragment).setOnClickListener {

        }
        findViewById<Button>(R.id.stack_count).setOnClickListener {

        }

    }


}