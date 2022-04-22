package com.component.kotlintest.demo.kotlin_demo

import android.text.TextWatcher
import android.widget.TextView

/**
 * @Author fox.hu
 * @Date 2020/4/23 16:14
 */

fun kotlinDSL(block: StringBuilder.() -> Unit) {
    block(StringBuilder("kotlin"))
}

fun TextView.addTextDsl(init:TextWatcher.()->Unit){


}
