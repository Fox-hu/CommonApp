package com.silver.fox.base


import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sliver.fox.base.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.reflect.ParameterizedType


abstract class BaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity(),
    CoroutineScope by MainScope() {

    protected lateinit var dataBinding: DB
    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
        dataBinding = DataBindingUtil.setContentView(
            this, getLayoutResId()
        )
        dataBinding.apply {
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
            setLifecycleOwner(this@BaseVMActivity)
        }
        initBaseObserver()
        bindDataAndEvent()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewModel.onActivityResultOK(requestCode, data)
        } else if (resultCode == RESULT_CANCELED) {
            viewModel.onActivityResultCancel(requestCode, data)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onActivityNewIntent(intent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        viewModel.onRestoreInstanceState(savedInstanceState, persistentState)
    }

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

    protected open fun onActivityResult(onActivityResultInfo: OnActivityResultInfo) {
        val resultCode: Int = onActivityResultInfo.resultCode
        val bundle: Bundle? = onActivityResultInfo.resultBundle
        setResult(resultCode, Intent().putExtras(bundle))
        finish()
    }

    /**
     * 当监听到ViewModel的信息时，展示新页面
     *
     * @param startActivityInfo 新页面的信息
     */
    fun startActivity(startActivityInfo: StartActivityInfo) {
        val intent: Intent = startActivityInfo.intent
        val requestCode: Int = startActivityInfo.requestCode
        if (requestCode == StartActivityInfo.NO_REQUEST_CODE) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }


    abstract fun bindDataAndEvent()
    abstract fun getLayoutResId(): Int

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}