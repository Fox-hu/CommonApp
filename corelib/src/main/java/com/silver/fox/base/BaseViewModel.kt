package com.silver.fox.base

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.silver.fox.Ktx
import com.silver.fox.SnackbarModel
import com.silver.fox.common.InitApp
import com.silver.fox.ext.getString
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


open class BaseViewModel(app: Application = InitApp.CONTEXT as Application) :
    AndroidViewModel(app),
    LifecycleObserver,
    KoinComponent {
    private var mActivityIntent: Intent? = null
    private var mFragmentArguments: Bundle? = null
    private val mShortToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    private val mLongToastMessage: SingleLiveEvent<String> = SingleLiveEvent()
    private val mWaitingDialogMessage: SingleLiveEvent<String> = SingleLiveEvent()
    private val mHideWaitingDialog: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val mStartActivityInfo: SingleLiveEvent<StartActivityInfo> = SingleLiveEvent()
    private val mOnActivityResultInfo: SingleLiveEvent<OnActivityResultInfo> = SingleLiveEvent()
    private val mFinishEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val mHideSoftKeyboard: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val snackbarData = MutableLiveData<SnackbarModel>()

    fun getShortToastMessage(): SingleLiveEvent<String> {
        return mShortToastMessage
    }

    fun getLongToastMessage(): SingleLiveEvent<String> {
        return mLongToastMessage
    }

    fun getWaitingDialogMessage(): SingleLiveEvent<String> {
        return mWaitingDialogMessage
    }

    fun getHideWaitingDialog(): SingleLiveEvent<Boolean> {
        return mHideWaitingDialog
    }

    fun getHideSoftKeyboard(): SingleLiveEvent<Boolean> {
        return mHideSoftKeyboard
    }

    fun getStartActivityInfo(): SingleLiveEvent<StartActivityInfo> {
        return mStartActivityInfo
    }

    fun getOnActivityResultInfo(): SingleLiveEvent<OnActivityResultInfo> {
        return mOnActivityResultInfo
    }

    fun getFinishEvent(): SingleLiveEvent<Boolean> {
        return mFinishEvent
    }

    fun startActivity(intent: Intent?) {
        if (intent == null) {
            return
        }
        mStartActivityInfo.value = StartActivityInfo(intent)
    }

    fun startActivity(t: Class<out Activity?>?) {
        startActivity(Intent(getApplication(), t))
    }

    fun startActivityForResult(
        t: Class<out Activity?>?,
        requestCode: Int
    ) {
        startActivityForResult(Intent(getApplication(), t), requestCode)
    }

    fun startActivityForResult(
        intent: Intent?,
        requestCode: Int
    ) {
        mStartActivityInfo.value = StartActivityInfo(intent!!, requestCode)
    }

    fun setResultAndFinish(bundle: Bundle?) {
        setResultAndFinish(Activity.RESULT_OK, bundle)
    }

    fun setResultAndFinish(resultCode: Int) {
        setResultAndFinish(resultCode, null)
    }

    fun setResultAndFinish(resultCode: Int, bundle: Bundle?) {
        mOnActivityResultInfo.setValue(OnActivityResultInfo(resultCode, bundle))
    }

    fun showToast(message: String?) {
        mShortToastMessage.value = message
    }

    fun showLongToast(message: String?) {
        mLongToastMessage.value = message
    }

    fun showWaitingDialog(message: String?) {
        mWaitingDialogMessage.value = message
    }

    fun hideWaitingDialog() {
        mHideWaitingDialog.value = true
    }


    fun showToast(@StringRes resId: Int) {
        showToast(resId.getString())
    }

    fun showLongToast(@StringRes resId: Int) {
        showLongToast(resId.getString())
    }

    fun showWaitingDialog(@StringRes resId: Int) {
        showWaitingDialog(resId.getString())
    }

    /**
     * 关闭当前页面
     */
    fun doFinish() {
        mFinishEvent.value = true
    }

    /**
     * 当页面接收到Intent时，由View分发过来
     *
     * @param intent startActivity时传入的intent
     */
    fun onActivityIntent(intent: Intent?) {
        mActivityIntent = intent
    }

    /**
     * activity会将onNewIntent被调用时传入的intent参数传给ViewModel来处理
     *
     * @param intent 重启页面时传入的Intent
     */
    fun onActivityNewIntent(intent: Intent) {
        mActivityIntent = intent
    }

    /**
     * 获取附属的数据
     */
    fun onFragmentArguments(bundle: Bundle?) {
        mFragmentArguments = bundle
    }

    fun getActivityIntent(): Intent? {
        return mActivityIntent
    }

    fun getFragmentArguments(): Bundle? {
        return mFragmentArguments
    }

    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     *
     *
     * 可在该回调方法里进行一些ui显示与隐藏
     *
     * @param isVisible true  不可见 -> 可见
     * false 可见  -> 不可见
     */
    fun onFragmentVisibleChange(isVisible: Boolean) {}

    /**
     * 在fragment首次可见时回调，可用于加载数据，防止每次进入都重复加载数据
     */
    fun onFragmentFirstVisible() {}

    /**
     * View的onActivityResult被调用时，如果resultCode == RESULT_OK，该方法会被调用
     *
     * @param requestCode 请求码
     * @param data        数据集
     * @see BaseFragment.onActivityResult
     * @see BaseActivity.onActivityResult
     */
    fun onActivityResultOK(
        requestCode: Int,
        data: Intent?
    ) {
    }

    /**
     * View的onActivityResult被调用时，如果resultCode == RESULT_CANCEL，该方法会被调用
     *
     * @param requestCode 请求码
     * @param data        数据集
     * @see BaseFragment.onActivityResult
     * @see BaseActivity.onActivityResult
     */
    fun onActivityResultCancel(
        requestCode: Int,
        data: Intent?
    ) {
    }

    /**
     * 当页面返回按钮被点击时，会将事件代理给viewModel
     *
     * @return 如果ViewModel需要拦截此次事件，比如弹出弹框，那么就返回true
     * 如果返回了false，则由activity去处理这次返回按钮的点击，
     * activity的默认操作时对activity包含的fragment栈进行回退，或者finish
     */
    fun onBackPressed(): Boolean {
        return false
    }

    /**
     * 页面异常销毁时的回调，可以保存一些基础的变量，在页面恢复时快速恢复数据
     *
     * @param outState 保存数据的bundle
     */
    fun onSaveInstanceState(outState: Bundle?) {}

    /**
     * 页面异常销毁然后恢复时，可以从中取出之前保存的数据
     *
     * @param savedInstanceState
     * @param persistentState
     */
    fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ): Unit {
    }

    /**
     * 下面的几个回调方法，可能涉及调用顺序关系，需要在具体的子类Activity和Fragment中调用
     * BaseActivity和BaseFragment不再标记当前页面状态
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
    }

    private val mException: MutableLiveData<Throwable> = MutableLiveData()

    private fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch { block() }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {}, true)
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit,
        handleCancellationExceptionManually: Boolean = false
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (e !is CancellationException || handleCancellationExceptionManually) {
                    mException.value = e
                    catchBlock(e)
                } else {
                    throw e
                }
            } finally {
                finallyBlock()
            }
        }
    }
}