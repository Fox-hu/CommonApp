package com.fox.toutiao.ui

import androidx.appcompat.widget.Toolbar

interface ToolbarManager {
    //接口中的toolbar变量
    val toolbar: Toolbar

    //接口中的String变量 已经设置好set get方法
    var toolbarTitle: String
        get() = toolbar.title.toString()
        set(value) {
            toolbar.title = value
        }
}