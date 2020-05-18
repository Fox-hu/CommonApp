package com.silver.fox.recycleview.holder

import android.view.View
import com.silver.fox.recycleview.pojo.EmptyPresenterModel
import com.silver.fox.recycleview.pojo.FooterPresenterModel
import com.silver.fox.view.R
import com.silver.fox.view.databinding.ViewCellEmptyBinding
import com.silver.fox.view.databinding.ViewCellLoadingMoreBinding

/**
 * @Author fox.hu
 * @Date 2020/5/18 13:56
 */
class EmptyBindingCell : DataBindingCell<EmptyPresenterModel, ViewCellEmptyBinding>() {

    override fun getLayoutResId(): Int = R.layout.view_cell_empty

    override fun bindData(vdb: ViewCellEmptyBinding?, item: EmptyPresenterModel, position: Int) {
        vdb?.apply {
            item.apply {
                ivEmpty.visibility = visibility
                tvEmpty.visibility = visibility
                tvEmpty.text = text
            }
        }
    }

    override fun bindView(vdb: ViewCellEmptyBinding) {
    }

}