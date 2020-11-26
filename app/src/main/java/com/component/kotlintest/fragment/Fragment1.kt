package com.component.kotlintest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.component.kotlintest.R

/**
 * @Author fox.hu
 * @Date 2020/11/3 16:45
 */
class Fragment1 : Fragment() {
    private val fragment2 by lazy {
        Fragment2()
    }
    private val fragment3 by lazy {
        Fragment3()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_1, container, false).apply {
            findViewById<Button>(R.id.back).setOnClickListener { activity?.onBackPressed() }
            findViewById<Button>(R.id.next_fragment).setOnClickListener {
                fragmentManager?.beginTransaction()?.replace(R.id.container, fragment2)?.addToBackStack("fragment2")?.commit()
            }
            findViewById<Button>(R.id.pre_fragment).setOnClickListener {
                fragmentManager?.beginTransaction()?.replace(R.id.container, fragment3)?.addToBackStack("fragment3")?.commit()
            }
        }
    }
}