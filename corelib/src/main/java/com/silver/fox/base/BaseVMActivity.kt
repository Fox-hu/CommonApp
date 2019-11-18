package com.silver.fox.base

import android.os.Bundle

abstract class BaseVMActivity<VM : BaseViewModel> : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserver()
    }

    abstract fun startObserver()
}