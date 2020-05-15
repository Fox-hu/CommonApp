package com.silver.fox.recycleview.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.silver.fox.recycleview.adapter.VHolder

/**
 * @Author fox.hu
 * @Date 2020/5/14 17:00
 */
abstract class DataBindingCell<T, VDB : ViewDataBinding> :
    VHolder<T, DataBindingCell.DataBindingViewHolder<VDB>>() {

    abstract fun bindData(vdb: VDB?, item: T, position: Int)

    abstract fun bindView(vdb: VDB)

    abstract fun getLayoutResId(): Int

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): DataBindingViewHolder<VDB> {
        val view = inflater.inflate(getLayoutResId(), parent, false)
        val binding: VDB? = DataBindingUtil.bind<VDB>(view)
        binding?.run {
            bindView(binding)
        }
        return DataBindingViewHolder(view, binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<VDB>, item: T, position: Int) {
        bindData(holder.dataBinding, item, position)
    }


    class DataBindingViewHolder<VDB : ViewDataBinding>(itemView: View, val dataBinding: VDB?) :
        RecyclerView.ViewHolder(itemView)
}