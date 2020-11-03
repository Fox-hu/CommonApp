package com.component.kotlintest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.component.kotlintest.R

/**
 * @Author fox.hu
 * @Date 2020/11/3 16:45
 */
class Fragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false).apply {
            findViewById<TextView>(R.id.title).text = "Fragment3"
            findViewById<Button>(R.id.back).setOnClickListener { activity?.onBackPressed() }
            findViewById<Button>(R.id.go_fragment).visibility = View.GONE
        }
    }
}