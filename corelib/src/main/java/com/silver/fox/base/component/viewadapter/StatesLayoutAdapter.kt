package com.silver.fox.base.component.viewadapter

import androidx.databinding.BindingAdapter
import com.fox.network.request.OriResult
import com.silver.fox.ext.logi
import com.silver.fox.stateslayout.PageState
import com.silver.fox.stateslayout.StatesLayout

@BindingAdapter("status")
fun StatesLayout.setStates(
    status: OriResult.Status?
) {
    status?.apply {
        "status = ${status.name}".logi("StatesLayout")
        when (status) {
            OriResult.Status.LOADING -> setState(PageState.INITIALIZING)
            OriResult.Status.ACTION_ERROR -> setState(PageState.ERROR)
            OriResult.Status.ACTION_FAIL -> setState(PageState.FAIL)
            OriResult.Status.ACTION_SUCCESS -> setState(PageState.NORMAL)
        }
    }
}