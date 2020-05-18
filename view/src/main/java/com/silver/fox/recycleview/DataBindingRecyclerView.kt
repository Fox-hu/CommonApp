package com.silver.fox.recycleview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silver.fox.ext.logi
import com.silver.fox.recycleview.datasource.DataLoader
import com.silver.fox.recycleview.datasource.DataSourceFactory
import com.silver.fox.recycleview.holder.EmptyBindingCell
import com.silver.fox.recycleview.holder.ErrorBindingCell
import com.silver.fox.recycleview.holder.FooterBindingCell
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

    private val adapterBuilder: DelegateAdapter.Builder = DelegateAdapter.Builder(this)

    var dataStatus = DataSourceFactory.Status.INITIALING
    var errorPresenterModel: ErrorPresenterModel = ErrorPresenterModel()
    var emptyPresenterModel: EmptyPresenterModel = EmptyPresenterModel()
    var footerPresenterModel: FooterPresenterModel = FooterPresenterModel()
    var loader: DataLoader? = null
    lateinit var dataSourceFactory: DataSourceFactory

    private val delegateAdapter: DelegateAdapter by lazy {
        adapterBuilder.apply {
            bind(FooterPresenterModel::class.java, FooterBindingCell())
            bind(EmptyPresenterModel::class.java, EmptyBindingCell())
            bind(ErrorPresenterModel::class.java, ErrorBindingCell())
        }.build()
    }

    init {
        setAnimDuration(0)
    }

    fun setLinearLayoutManager() {
        adapter = delegateAdapter
        layoutManager = LinearLayoutManager(context)
    }

    fun loadData() {
        if (loader == null) {
            throw IllegalStateException("loader must not null")
        }
        dataStatus = DataSourceFactory.Status.INITIALING
        dataSourceFactory = DataSourceFactory(loader!!).apply {
            status.observeForever(::setStatus)
        }

    }

    private fun setStatus(status: DataSourceFactory.Status) {
        "DataSourceStatus = $status".logi("DataBindingRecyclerView")
        dataStatus = status
        when (dataStatus) {
            DataSourceFactory.Status.INITIAL_SUCCESS -> post { scrollToPosition(0) }
            DataSourceFactory.Status.INITIAL_FAIL -> dataSourceFactory.checkRetry()
            DataSourceFactory.Status.LOADING, DataSourceFactory.Status.LOAD_SUCCESS, DataSourceFactory.Status.LOAD_FAIL -> {
                delegateAdapter.apply {
                    if (getItem(itemCount - 1) != null) {
                        notifyItemChanged(itemCount - 1)
                    } else {
                        statusChangeNotify()
                    }
                }
            }
            DataSourceFactory.Status.COMPLETE -> {

            }
        }
    }


    private fun setAnimDuration(duration: Long) {
        itemAnimator?.apply {
            addDuration = duration
            changeDuration = duration
            moveDuration = duration
            removeDuration = duration
        }
    }
}