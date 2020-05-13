package com.silver.fox.base.component.ui

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.databinding.ViewDataBinding
import com.silver.fox.base.component.viewmodel.BaseViewModel

/**
 * @Author fox.hu
 * @Date 2020/5/13 11:01
 */
abstract class BaseLazyLoadVMFragment<VM : BaseViewModel, VDB : ViewDataBinding> :
    BaseVMFragment<VM, VDB>() {
    protected var isViewInitiated = false
    protected var isVisibleToUser = false
    protected var isDataInitiated = false

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewInitiated = true
        prepareFetchData()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            prepareFetchData()
        }
    }

    protected open fun prepareFetchData() {
        if (isViewInitiated && isVisibleToUser && !isDataInitiated) {
            viewModel.onFragmentFirstVisible()
            isDataInitiated = true
        }
    }
}