package com.silver.fox.base


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.sliver.fox.base.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType


abstract class BaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity(), CoroutineScope by MainScope() {

    protected lateinit var dataBinding: DB
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(
            this, getLayoutResId()
        )
        dataBinding.apply {
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
            setLifecycleOwner(this@BaseVMActivity)
        }

        initView()
        startObserver()
    }

    abstract fun initView()
    abstract fun getLayoutResId(): Int
    abstract fun startObserver()

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}