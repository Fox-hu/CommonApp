package com.silver.fox.recycleview

import androidx.databinding.ViewDataBinding

/**
 * @Author fox.hu
 * @Date 2020/5/14 15:51
 */

interface OnItemClickListener<VDB : ViewDataBinding> {
    fun onItemClick(vdb: VDB)
}

interface OnItemLongClickListener<VDB : ViewDataBinding> {
    fun onItemLongClick(vdb: VDB)
}

interface OnItemViewBindCallBack<VDB : ViewDataBinding> {
    fun onItemViewBind(vdb: VDB, position: Int)
}

interface OnItemViewCreateCallBack<VDB : ViewDataBinding> {
    fun onItemCreate(vdb: VDB)
}
