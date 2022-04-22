package com.component.kotlintest.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.component.kotlintest.R

/**
 * @Author fox.hu
 * @Date 2020/11/3 16:45
 */
class Fragment3 : Fragment(){

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_3, container, false).apply {
            findViewById<Button>(R.id.back).setOnClickListener { activity?.onBackPressed() }
            findViewById<Button>(R.id.pre_fragment).apply {
                setOnClickListener {
                    fragmentManager?.popBackStack("fragment1", 0)
                }
            }
            findViewById<Button>(R.id.clear_all).setOnClickListener {
                fragmentManager?.popBackStackImmediate("fragment1", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}