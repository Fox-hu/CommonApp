package com.silver.fox.stateslayout

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.silver.fox.ext.getString
import com.silver.fox.view.R

class LoadingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    protected lateinit var mLoadingView: View
    protected lateinit var mLoading: ProgressBar

    lateinit var loadingTextView: TextView
    private var mCallback: CommonLoadClick? = null
    protected lateinit var mBgLayout: LinearLayout
    protected var mClickLayout: RelativeLayout? = null

    init {
        initView(context)
    }

    private fun initView(context: Context?) {
        val inflater = LayoutInflater.from(context)
        mLoadingView = inflater.inflate(R.layout.view_cell_loading, this, true)
        mLoading =
            mLoadingView.findViewById<View>(R.id.loading_ring_progress) as ProgressBar
        loadingTextView =
            mLoadingView.findViewById<View>(R.id.textView1) as TextView
        mBgLayout =
            mLoadingView.findViewById<View>(R.id.loadingbg) as LinearLayout
        mClickLayout =
            mLoadingView.findViewById<View>(R.id.common_loading_layout) as RelativeLayout
        showLoadingView()
    }

    /**
     * 设置背景
     */
    fun setBackground(resid: Int) {
        mClickLayout!!.setBackgroundResource(resid)
    }

    fun setmBgLayoutBackgroud(resid: Int) {
        mBgLayout.setBackgroundResource(resid)
    }

    /**
     * 设置加载文案
     */
    fun setLoadingText(text: String?) {
        loadingTextView.text = text
    }
    /**
     * 设置自定义错误信息
     */
    /**
     * 默认错误信息
     */
    @JvmOverloads
    fun showErrorLoadingView(errortext: String = "") {
        loadingTextView.text =
            if ("" == errortext) R.string.view_error_click_to_reload.getString(context) else errortext
        mLoadingView.visibility = View.VISIBLE
        loadingTextView.setTextColor(resources.getColor(R.color.view_666666))
        mLoadingView.findViewById<View>(R.id.common_loading_layout).setOnClickListener(this)
        mLoading.visibility = View.GONE
    }

    /**
     * 设置自定义错误信息
     * tingli.li
     * 2017-11-24
     */
    fun showErrorLoadingView(errortext: String, color: Int, size: Int) {
        loadingTextView.text =
            if ("" == errortext) R.string.view_error_click_to_reload.getString(context) else errortext
        mLoadingView.visibility = View.VISIBLE
        loadingTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
        loadingTextView.setTextColor(resources.getColor(color))
        val textPaint = loadingTextView.paint
        textPaint.isFakeBoldText = false
        textPaint.typeface = Typeface.DEFAULT
        mLoadingView.findViewById<View>(R.id.common_loading_layout)
            .setOnClickListener(this)
        mLoading.visibility = View.GONE
    }
    /**
     * 显示自定义加载文案
     */
    /**
     * 显示默认加载中
     */
    @JvmOverloads
    fun showLoadingView(text: String = "") {
        loadingTextView.text =
            if ("" == text) R.string.view_common_loading.getString(context) else text
        loadingTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        loadingTextView.setTextColor(resources.getColor(R.color.view_999999))
        val textPaint = loadingTextView.paint
        textPaint.isFakeBoldText = true
        textPaint.typeface = Typeface.DEFAULT_BOLD
        mLoading.visibility = View.VISIBLE
        mLoadingView.visibility = View.VISIBLE
    }

    /**
     * 隐藏加载布局
     */
    fun hiddenLoadingView() {
        mLoadingView.visibility = View.GONE
    }

    /**
     * 设置点击事件
     */
    fun setOnLoadingClick(click: CommonLoadClick?) {
        if (null == click) {
            return
        }
        mCallback = click
        mLoadingView.setOnClickListener(this)
    }

    /**
     * 设置当前加载圈是否可以点击
     * Modified by oliver.chen 2015-3-24
     */
    fun setLoadingViewIsClickable(isClickable: Boolean?) {
        mClickLayout!!.isClickable = isClickable!!
    }

    interface CommonLoadClick {
        /* 加载布局点击回调 */
        fun onCommonLoadClick(viewId: Int)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.common_loading_layout) {
            if (mLoading.visibility == View.VISIBLE) {
                return
            }
            showLoadingView()
            if (null != mCallback) {
                mCallback!!.onCommonLoadClick(v.id)
            }
        }
    }
}