package com.silver.fox.recycleview.holder

import android.view.View
import com.silver.fox.recycleview.pojo.EmptyPresenterModel
import com.silver.fox.recycleview.pojo.ErrorPresenterModel
import com.silver.fox.recycleview.pojo.FooterPresenterModel
import com.silver.fox.view.R
import com.silver.fox.view.databinding.ViewCellEmptyBinding
import com.silver.fox.view.databinding.ViewCellErrorBinding
import com.silver.fox.view.databinding.ViewCellLoadingMoreBinding

/**
 * @Author fox.hu
 * @Date 2020/5/18 13:56
 */
class ErrorBindingCell : DataBindingCell<ErrorPresenterModel, ViewCellErrorBinding>() {

    override fun getLayoutResId(): Int = R.layout.view_cell_error

    override fun bindData(vdb: ViewCellErrorBinding?, item: ErrorPresenterModel, position: Int) {
        vdb?.apply {
            item.apply {
                ivError.visibility = visibility
                tvError.visibility = visibility
                tvError.text = text
            }
        }
    }

    override fun bindView(vdb: ViewCellErrorBinding) {
    }

}