package com.component.kotlintest.fragment

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.component.kotlintest.R
import org.jetbrains.anko.toast

/**
 * @Author fox.hu
 * @Date 2020/11/3 16:19
 */
class FragmentActivity : AppCompatActivity() {
    private val fragment1 by lazy {
        supportFragmentManager.findFragmentByTag(Fragment1::javaClass.name) ?: Fragment1()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        findViewById<Button>(R.id.go_fragment).setOnClickListener {
            supportFragmentManager.apply {
                beginTransaction().apply {
//                    add(R.id.container, fragment1, Fragment1::javaClass.name).commit()
                    val commit = replace(R.id.container, fragment1).addToBackStack("fragment1").commit()
                    toast(commit.toString())
                }
            }
        }

        findViewById<Button>(R.id.stack_count).setOnClickListener {
            toast(supportFragmentManager.backStackEntryCount.toString())
        }

        findViewById<Button>(R.id.stack_name).setOnClickListener {
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                val backStackEntryAt = supportFragmentManager.getBackStackEntryAt(i)
                toast(backStackEntryAt.name.toString())
            }
        }
    }

}