package com.silver.fox.stateslayout

import NormalLayout
import android.content.Context

import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.silver.fox.ext.getColor
import com.silver.fox.view.R

import java.util.*

/**
 * create by francis
 * on date 2018-07-10
 */
class StatesLayout @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(mContext, attrs, defStyleAttr) {
    private var mPageState = ""
    private val mLoadingParam: StateParam
    private val mFailParam: StateParam
    private val mErrorParam: StateParam
    private lateinit var mNormalParam: StateParam
    private val mPageStatesMap = HashMap<String, StateParam>()
    @ColorInt
    private val mDefaultBackgroundColor: Int = com.silver.fox.view.R.color.view_grey_fafafa.getColor(mContext)

    init {
        //这里还需要考虑自定义状态页面时的处理，暂时不用管
        val loadingLayout = LoadingLayout(mContext)
        val failLayout = FailLayout(mContext)
        val errorLayout = ErrorLayout(mContext)
        mLoadingParam = StateParam(loadingLayout)
        mLoadingParam.text.set(resources.getString(R.string.view_common_text_data_loading))
        mPageStatesMap[PageState.INITIALIZING.VALUE] = mLoadingParam

        mFailParam = StateParam(failLayout)
        mFailParam.drawableRes.set(R.drawable.common_feedback_empty)
        mFailParam.text.set(resources.getString(R.string.view_common_data_empty))
        mPageStatesMap[PageState.FAIL.VALUE] = mFailParam

        mErrorParam = StateParam(errorLayout)
        mErrorParam.drawableRes.set(R.drawable.common_feedback_error)
        mErrorParam.text.set(resources.getString(R.string.view_error_click_to_reload))
        mPageStatesMap[PageState.ERROR.VALUE] = mErrorParam

        initAttrs(mContext, attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.view_StatesLayout)
        @DrawableRes val loadingImage = a.getResourceId(R.styleable.view_StatesLayout_view_loadingImageSrc, 0)
        @DrawableRes val failImage = a.getResourceId(R.styleable.view_StatesLayout_view_failImageSrc, R.drawable.common_feedback_empty)
        val failText = a.getString(R.styleable.view_StatesLayout_view_failText)
        val failButtonText = a.getString(R.styleable.view_StatesLayout_view_failButtonText)
        @DrawableRes val errorImage = a.getResourceId(R.styleable.view_StatesLayout_view_errorImageSrc, R.drawable.common_feedback_error)
        val errorText = a.getString(R.styleable.view_StatesLayout_view_errorText)
        val errorButtonText = a.getString(R.styleable.view_StatesLayout_view_errorButtonText)

        if (loadingImage != 0) {
            mLoadingParam.drawableRes.set(loadingImage)
            val frameImageLoadingLayout = FrameImageLoadingLayout(mContext)
            mLoadingParam.stateLayout = frameImageLoadingLayout
        }
        mFailParam.drawableRes.set(failImage)
        if (failText != null) {
            mFailParam.text.set(failText)
        }
        if (failButtonText != null) {
            mFailParam.buttonText.set(failButtonText)
        }

        mErrorParam.drawableRes.set(errorImage)
        if (errorText != null) {
            mErrorParam.text.set(errorText)
        }
        if (errorButtonText != null) {
            mErrorParam.buttonText.set(errorButtonText)
        }
        a.recycle()
    }

    /**
     * 初始化状态页面，将几个状态全部加进去
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        if (background == null) {
            this.setBackgroundColor(mDefaultBackgroundColor)
        }
        val normalLayout = NormalLayout(getChildAt(0))
        mNormalParam = StateParam(normalLayout)
        mNormalParam.stateLayout = normalLayout
        mPageStatesMap[PageState.NORMAL.VALUE] = mNormalParam
    }

    /**
     * 获取状态对应的数据信息，可以直接更改
     */
    fun getStateParam(pageState: PageState): StateParam? {
        return mPageStatesMap[pageState.VALUE]
    }

    /**
     * 允许用户添加自定义页面状态
     */
    fun addState(code: String, stateParam: StateParam) {
        this.mPageStatesMap[code] = stateParam
    }

    /**
     * 常规用法，直接使用已经预定义的状态
     *
     * @see .setCustomState
     */
    fun setState(pageState: PageState) {
        setCustomState(pageState.VALUE)
    }

    /**
     * 拓展用法，允许用于输入自定已状态值
     */
    fun setCustomState(pageState: String) {
        if (pageState != mPageState) {
            mPageState = pageState
            removeAllViews()
            if (mPageStatesMap.containsKey(pageState)) {
                val stateParam = mPageStatesMap[pageState]!!
                val stateLayout = stateParam.stateLayout
                val stateView = stateLayout.getStateView()
                stateLayout.bindData(stateParam)
                val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                addView(stateView, params)
                invalidate()
            }
        }

    }

    fun setOnReloadListener(listener: () -> Unit) {
        val viewModel = object : StateLayoutViewModel() {
            override fun onReloadClick() {
                listener.invoke()
            }
        }
        mPageStatesMap.values.forEach { it.viewModel = viewModel }
    }

    fun setStateLayoutCallBack(callback: StateLayoutCallback) {
        mPageStatesMap.values.forEach { it.viewModel = callback }
    }
}


