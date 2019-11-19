package com.silver.fox.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserver()
    }

    protected lateinit var dataBinding: DB
    protected abstract val viewModel: VM

    override fun setContentView(layoutResID: Int) {
        dataBinding = DataBindingUtil.inflate(layoutInflater, getLayoutResId(), null, false)
//        dataBinding.setVariable(BR.viewModel, viewModel)
        dataBinding.executePendingBindings()
        super.setContentView(dataBinding.root)
    }

    abstract fun startObserver()
}