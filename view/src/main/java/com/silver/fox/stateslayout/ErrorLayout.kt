package com.silver.fox.stateslayout

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.silver.fox.view.R
import com.silver.fox.view.BR

/**
 * @author francis.fan 2018/7/18
 */
class ErrorLayout(private val mContext: Context) : StateLayout {
    private var mViewDataBinding: ViewDataBinding? = null

    override fun getStateView(): View {
        if (mViewDataBinding == null) {
            val inflater = LayoutInflater.from(mContext)
            mViewDataBinding = DataBindingUtil.inflate(inflater,
                    R.layout.view_common_abnormal_layout, null, false)
        }
        return mViewDataBinding!!.root
    }

    override fun bindData(stateParam: StateParam) {
        mViewDataBinding?.let { binding ->
            binding.setVariable(BR.presenterModel, stateParam)
            stateParam.viewModel?.let {
                binding.setVariable(BR.viewModel, it)
            }
        }
    }
}