package com.silver.fox.base


import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sliver.fox.corelib.BR


abstract class BaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserver()
    }

    protected lateinit var dataBinding: DB
    protected abstract val viewModel: VM

    override fun setContentView(layoutResID: Int) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(this), layoutResID, null, false)

        dataBinding.setVariable(BR.viewModel,viewModel)
        dataBinding.executePendingBindings()
        super.setContentView(dataBinding.root)
    }

    abstract fun startObserver()
}