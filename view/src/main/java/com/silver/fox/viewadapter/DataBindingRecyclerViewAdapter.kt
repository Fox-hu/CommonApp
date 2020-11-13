package com.silver.fox.viewadapter


import androidx.databinding.BindingAdapter
import com.silver.fox.recycleview.DataBindingRecyclerView

/**
 * @Author fox.hu
 * @Date 2020/4/2 11:15
 */

@BindingAdapter("dataList")
fun setType(recyclerView: DataBindingRecyclerView, dataList: List<Any>) {
    recyclerView.submitData(dataList)
}