package com.silver.fox.recycleview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.silver.fox.recycleview.datasource.DataSourceFactory
import com.silver.fox.recycleview.pojo.EmptyPresenterModel
import com.silver.fox.recycleview.pojo.ErrorPresenterModel
import com.silver.fox.recycleview.pojo.FooterPresenterModel

/**
 * @Author fox.hu
 * @Date 2020/5/15 15:05
 */
class DataBindingRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    var dataStatus = DataSourceFactory.Status.INITIALING
    lateinit var delegateAdapter: DelegateAdapter
    lateinit var errorPresenterModel: ErrorPresenterModel
    lateinit var emptyPresenterModel: EmptyPresenterModel
    lateinit var footerPresenterModel: FooterPresenterModel
    lateinit var dataSourceFactory: DataSourceFactory
}