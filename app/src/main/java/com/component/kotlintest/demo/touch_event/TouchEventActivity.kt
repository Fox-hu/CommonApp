package com.component.kotlintest.demo.touch_event

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.component.kotlintest.R
import kotlinx.android.synthetic.main.activity_touch.*

/**
 * @Author fox.hu
 * @Date 2020/11/27 15:51
 */
class TouchEventActivity : AppCompatActivity() {

    private val data = arrayOf(
        "Apple", "Banana", "Orange", "Watermelon",
        "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango",
        "Apple", "Banana", "Orange", "Watermelon",
        "Pear", "Grape", "Pineapple", "Strawberry", "Cherry", "Mango"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_touch)
        showList()
    }

    private fun showList() {
        demo_lv.adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data)
    }
}