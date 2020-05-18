package com.silver.fox.recycleview.holder

import com.silver.fox.recycleview.pojo.FooterPresenterModel
import com.silver.fox.view.R
import com.silver.fox.view.databinding.ViewCellLoadingMoreBinding

/**
 * @Author fox.hu
 * @Date 2020/5/18 13:56
 */
class FooterBindingCell : DataBindingCell<FooterPresenterModel, ViewCellLoadingMoreBinding>() {

    override fun getLayoutResId(): Int = R.layout.view_cell_loading_more

    override fun bindData(
        vdb: ViewCellLoadingMoreBinding?,
        item: FooterPresenterModel,
        position: Int
    ) {
        vdb?.apply {
            footer = item
        }
    }

    override fun bindView(vdb: ViewCellLoadingMoreBinding) {

    }

}