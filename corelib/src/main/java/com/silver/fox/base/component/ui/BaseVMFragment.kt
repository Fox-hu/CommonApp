package com.silver.fox.base.component.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.silver.fox.base.component.bean.OnActivityResultInfo
import com.silver.fox.base.component.bean.StartActivityInfo
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.sliver.fox.base.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseVMFragment<VM : BaseViewModel, VDB : ViewDataBinding> : Fragment(),
    CoroutineScope by MainScope() {

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
//            executePendingBindings()
            lifecycleOwner = viewLifecycleOwner
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindDataAndEvent()
    }

    abstract fun getLayoutResId(): Int

    protected open fun initBaseObserver() {
        viewModel.mShortToastMessage.observe(this, Observer { showToast(it) })
        viewModel.mLongToastMessage.observe(this, Observer { showLongToast(it) })
        viewModel.mWaitingDialogMessage.observe(this, Observer { showWaitingDialog(it) })
        viewModel.mHideWaitingDialog.observe(this, Observer { hideWaitingDialog(it) })
        viewModel.mFinishEvent.observe(this, Observer { doFinish(it) })
        viewModel.mStartActivityInfo.observe(this, Observer { startActivity(it) })
        viewModel.mOnActivityResultInfo.observe(this, Observer { onActivityResult(it) })
        viewModel.mHideSoftKeyboard.observe(this, Observer { hideSoftKeyboard(it) })
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
        activity?.setResult(resultCode, if (bundle != null) Intent().putExtras(bundle) else Intent())
    }

    abstract fun bindDataAndEvent()

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun onSaveInstanceState(@NonNull outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        viewModel.onSaveInstanceState(outState)
    }

    open fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        viewModel.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            viewModel.onActivityResultOK(requestCode, data)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            viewModel.onActivityResultCancel(requestCode, data)
        }
    }

}