package com.silver.fox.recycleview.holder

import androidx.databinding.ViewDataBinding

/**
 * @Author fox.hu
 * @Date 2020/5/18 10:51
 */
class NormalCell<VDB : ViewDataBinding>(val cell: Cell<VDB>) : DataBindingCell<Any, VDB>() {

    override fun bindData(vdb: VDB?, item: Any, position: Int) {
        cell.apply {
            vdb?.apply {
                setVariable(presentModelId, item)
                executePendingBindings()
                clickCallBack?.apply {
                    root.setOnClickListener { onItemClick(vdb) }
                }
                longClickCallBack?.apply {
                    root.setOnLongClickListener {
                        onItemLongClick(vdb)
                        true
                    }
                }
                bindCallBack?.onItemViewBind(vdb, position)
            }
        }
    }

    override fun bindView(vdb: VDB) {
        cell.apply {
            if (viewModelId != 0 && viewModel != null) {
                vdb.setVariable(viewModelId, viewModel)
            }
            createCallBack?.onItemCreate(vdb)
        }
    }

    override fun getLayoutResId(): Int = cell.layoutId

}