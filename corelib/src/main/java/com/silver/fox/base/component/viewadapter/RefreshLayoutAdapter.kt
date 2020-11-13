package com.silver.fox.base.component.viewadapter

import androidx.databinding.BindingAdapter
import com.fox.network.request.OriResult
import com.silver.fox.ext.logi
import com.silver.fox.recycleview.DataBindingRefreshLayout

@BindingAdapter("status")
fun DataBindingRefreshLayout.refresh(
    status: OriResult.Status?
) {
    status?.apply {
        "status = ${status.name}".logi("DataBindingRefreshLayout")
        when (status) {
            OriResult.Status.LOADING -> autoRefresh()
            else -> stopRefresh()
        }
    }
}
