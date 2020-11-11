package com.silver.fox.base.component.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.silver.fox.base.component.bean.OnActivityResultInfo
import com.silver.fox.base.component.bean.StartActivityInfo
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.hideKeyboard
import com.sliver.fox.base.BR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseVMFragment<VM : BaseViewModel, VDB : ViewDataBinding> : Fragment(),
    CoroutineScope by MainScope() {

    protected lateinit var dataBinding: ViewDataBinding
    protected lateinit var currentActivity: Activity
    abstract val viewModel: VM

    private var startActivityInfo: StartActivityInfo? = null
    private var rootView: View? = null
    private var isFragmentVisible = false
    private var isFirstVisible = false
    private var isReuseView = false
    private var isFragmentActive = false
    private var mToastMessage: String? = null
    private var mLongToastMessage: String? = null
    private var mWaitingDialogMessage: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentActivity = context as Activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
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
            lifecycleOwner = viewLifecycleOwner
        }
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        if (rootView == null) {
            rootView = view
        }
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
        mToastMessage = toastMessage
        if (isFragmentVisible && !TextUtils.isEmpty(toastMessage)) {
//            TipDialog.showTips(activity, toastMessage)
            mToastMessage = null
        }
    }

    fun showLongToast(toastMessage: String) {
        mLongToastMessage = toastMessage
        if (isFragmentVisible && !TextUtils.isEmpty(toastMessage)) {
//            TipDialog.showLongTips(activity, toastMessage)
            mLongToastMessage = null
        }
    }

    fun showWaitingDialog(msg: String) {
        mWaitingDialogMessage = msg
        if (isFragmentVisible && mWaitingDialogMessage != null) {
//            TipDialog.showWaitingTips(mActivity, msg)
            mWaitingDialogMessage = null
        }
    }

    fun hideWaitingDialog(hide: Boolean) {
    }

    fun hideSoftKeyboard(hide: Boolean) {
        currentActivity.hideKeyboard()
    }

    fun doFinish(finish: Boolean) {
        currentActivity.finish()
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
        activity?.setResult(
            resultCode,
            if (bundle != null) Intent().putExtras(bundle) else Intent()
        )
    }

    abstract fun bindDataAndEvent()

    override fun onDestroy() {
        super.onDestroy()
        initVariable()
        cancel()
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

    open fun onBackPressed(isUIBackPressed: Boolean): Boolean {
        val isViewModelTakeOver: Boolean = viewModel.onBackPressed()
        if (isUIBackPressed) {
            if (!isViewModelTakeOver) {
                //
            }
        }
        return isViewModelTakeOver
    }

    open fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        viewModel.onRestoreInstanceState(savedInstanceState, persistentState)
    }

    open fun onNewIntent(intent: Intent?) {
        if (intent != null) {
            viewModel.onActivityNewIntent(intent)
        }
    }

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    //另：在viewpager嵌套viewpager时，父viewpager切换item时，只有父viewpager下的第一层的Fragment会回调setUserVisibleHint方法
    //如：NavigationBarFragment的viewpager切换item时，只有PositionHomeFragment，ReportHomeFragment等5个底部tab对应的Fragment会回调setUserVisibleHint方法
    //   而PositionHomeFragment下的PositionFullJobFragment，PositionInternshipFragment等Fragment不会回调
    //再另：在viewpager嵌套viewpager时，父viewpager切换item时，子viewpager自身显示的Fragment的mUserVisibleHint仍然为true。
    //如：从PositionHomeFragment切换到ReportHomeFragment时，PositionHomeFragment的mUserVisibleHint变为了false，
    //   但PositionHomeFragment的viewpager当前position对应fragment的mUserVisibleHint仍然为true
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (rootView == null) {
            return
        }
        parseFragmentVisibility()
        //父ViewPager切换的时候回调其子ViewPager中的所有Fragment的setUserVisibleHint方法
        val childFragmentList =
            childFragmentManager.fragments
        if (childFragmentList != null && childFragmentList.size > 0) {
            for (childFragment in childFragmentList) {
                childFragment.userVisibleHint = childFragment.userVisibleHint
            }
        }
    }

    /**
     * 分析Fragment是否可见
     */
    private fun parseFragmentVisibility() {
        // 如果Fragment不处于onResume和onPause之间，则应该是不可见状态
        var isFragmentVisible =
            userVisibleHint && isFragmentActive && !isHidden
        // 如果自己是可见状态，但父Fragment却是不可见状态，那么自己也应该是不可见状态
        if (isFragmentVisible) {
            val parentFragment = parentFragment
            if (parentFragment is BaseVMFragment<*, *>
                && !(parentFragment as BaseVMFragment<*, *>?)?.isFragmentVisible!!
            ) {
                isFragmentVisible = false
            }
        }
        //第一次显示的回调方法
        if (isFirstVisible && isFragmentVisible) {
            isFirstVisible = false
            onFragmentFirstVisible()
        }
        //Visibility发生变化的回调方法
        if (isFragmentVisible != this.isFragmentVisible) {
            this.isFragmentVisible = isFragmentVisible
            onFragmentVisibleChange(this.isFragmentVisible)
        }
    }

    open fun isFragmentVisible(): Boolean {
        return isFragmentVisible
    }

    //使用onStart和onStop在进入页面快速点击返回键时原页面没有回调到onFragmentVisibleChange，故改成onResume和onPause。
    override fun onResume() {
        super.onResume()
        isFragmentActive = true
        parseFragmentVisibility()
    }

    override fun onPause() {
        super.onPause()
        isFragmentActive = false
        parseFragmentVisibility()
    }

    override fun onHiddenChanged(isHidden: Boolean) {
        super.onHiddenChanged(isHidden)
        parseFragmentVisibility()
    }

    private fun initVariable() {
        isFirstVisible = true
        isFragmentVisible = false
        isFragmentActive = false
        rootView = null
        isReuseView = true
    }

    /**
     * 是否复用view
     *
     * @param isReuse 是否复用view
     */
    protected open fun reuseView(isReuse: Boolean) {
        isReuseView = isReuse
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
    @CallSuper
    protected open fun onFragmentVisibleChange(isVisible: Boolean) {
        viewModel.onFragmentVisibleChange(isVisible)
        if (isVisible) {
            if (mToastMessage != null && mToastMessage!!.isNotEmpty()) {
                showToast(mToastMessage!!)
            }
            if (mLongToastMessage != null && mLongToastMessage!!.isNotEmpty()) {
                showLongToast(mLongToastMessage!!)
            }
            if (mWaitingDialogMessage != null && mWaitingDialogMessage!!.isNotEmpty()) {
                showWaitingDialog(mWaitingDialogMessage!!)
            }
            startActivityInfo?.let { startActivity(it) }
        }
    }

    /**
     * 在fragment首次可见时回调，可用于加载数据，防止每次进入都重复加载数据
     */
    protected open fun onFragmentFirstVisible() {
        viewModel.onFragmentFirstVisible()
    }
}