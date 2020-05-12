package com.silver.fox.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.sliver.fox.base.BR

abstract class BaseVMFragment<VM : BaseViewModel, VDB : ViewDataBinding> : BaseFragment() {

    protected lateinit var dataBinding: ViewDataBinding
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        viewModel.onActivityIntent(activity?.intent)
        viewModel.onFragmentArguments(arguments)
        initBaseObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        dataBinding.apply {
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
            setLifecycleOwner(this@BaseVMFragment)
        }
        bindDataAndEvent()
        return dataBinding.root
    }

    abstract fun getLayoutResId(): Int

    protected open fun initBaseObserver() {
        viewModel.getShortToastMessage()
            .observe(this, Observer { showToast(it) })
        viewModel.getLongToastMessage()
            .observe(this, Observer { showLongToast(it) })
        viewModel.getWaitingDialogMessage()
            .observe(this, Observer { showWaitingDialog(it) })
        viewModel.getHideWaitingDialog()
            .observe(this, Observer { hideWaitingDialog(it) })
        viewModel.getFinishEvent()
            .observe(this, Observer { doFinish(it) })
        viewModel.getStartActivityInfo().observe(this,
            Observer { startActivity(it) })
        viewModel.getOnActivityResultInfo().observe(this, Observer { onActivityResult(it) })
        viewModel.getHideSoftKeyboard()
            .observe(this, Observer { hideSoftKeyboard(it) })
    }

//    利用泛型 获取class
//    private fun createViewModel(): VM {
//        val modelClass =
//            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
//        return ViewModelProvider(this).get(modelClass)
//    }

    fun showToast(toastMessage: String) {
    }

    fun showLongToast(toastMessage: String) {

    }

    fun showWaitingDialog(msg: String) {

    }

    fun hideWaitingDialog(hide: Boolean) {

    }

    fun hideSoftKeyboard(hide: Boolean) {

    }

    fun doFinish(finish: Boolean) {

    }

    fun startActivity(startActivityInfo: StartActivityInfo) {
        val intent = startActivityInfo.intent
        val requestCode = startActivityInfo.requestCode
        if (requestCode == StartActivityInfo.NO_REQUEST_CODE) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    fun onActivityResult(onActivityResultInfo: OnActivityResultInfo) {
        val resultCode: Int = onActivityResultInfo.resultCode
        val bundle: Bundle? = onActivityResultInfo.resultBundle
        activity?.setResult(resultCode, Intent().putExtras(bundle))
    }

    abstract fun bindDataAndEvent()
}