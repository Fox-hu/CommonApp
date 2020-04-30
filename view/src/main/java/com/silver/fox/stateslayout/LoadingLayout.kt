package com.silver.fox.stateslayout

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.databinding.Observable


import com.silver.fox.stateslayout.StateLayout
import com.silver.fox.stateslayout.StateParam

/**
 * @author francis.fan 2018/7/18
 * 默认使用的LoadingLayout
 * 只可以配置加载过程中的文字
 */
class LoadingLayout(private val mContext: Context) : StateLayout {

    private var mLoadingLayout: TextView? = null
    private var tvLoading: TextView? = null

    override fun getStateView(): View {
        if (mLoadingLayout == null) {
            tvLoading = mLoadingLayout
        }
        return mLoadingLayout!!
    }

    override fun bindData(stateParam: StateParam) {
        tvLoading!!.text = stateParam.text.get()
        stateParam.text.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable, propertyId: Int) {
                tvLoading!!.text = stateParam.text.get()
            }
        })
    }
}