package com.silver.fox.recycleview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silver.fox.ext.logi
import com.silver.fox.recycleview.adapter.DelegateAdapter
import com.silver.fox.recycleview.datasource.DataLoader
import com.silver.fox.recycleview.datasource.DataSourceFactory
import com.silver.fox.recycleview.datasource.toPagedList
import com.silver.fox.recycleview.holder.*
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

    var dataSourceFactory: DataSourceFactory? = null
    lateinit var dataList: LiveData<PagedList<Any>>
    var pageSize = 30

    private val delegateAdapter: DelegateAdapter by lazy {
        adapterBuilder.apply {
            bind(FooterPresenterModel::class.java, FooterBindingCell())
            bind(EmptyPresenterModel::class.java, EmptyBindingCell())
            bind(ErrorPresenterModel::class.java, ErrorBindingCell())
        }.build()
    }

    private var refresh: SwipeRefreshLayout? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        refresh = findRefresh(this)
        refresh?.setOnRefreshListener {
            refreshData()
        }
    }

    init {
        setAnimDuration(0)
    }

    fun <VDB : ViewDataBinding> bind(cell: Cell<VDB>) {
        cell.presentModelClass?.let { adapterBuilder.bind(it,NormalCell(cell)) }
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
        dataList = LivePagedListBuilder(dataSourceFactory!!, pagedConfig(pageSize)).build().apply {
            observeForever(::setData)
        }
    }

    fun setLoaderAndInit(loader: DataLoader) {
        this.loader = loader
        loadData()
    }

    fun submitData(data: List<Any>) {
        val pagedList = data.toPagedList()
        loader?.list = pagedList
        dataList = MutableLiveData()
        val mutableLiveData = dataList as MutableLiveData
        mutableLiveData.value = pagedList
    }

    private fun refreshData() {
        if (loader == null) return
        if (dataSourceFactory == null) {
            loadData()
            return
        }
    }

    private fun findRefresh(view: View): SwipeRefreshLayout? {
        return when (view.parent) {
            null -> {
                null
            }
            is SwipeRefreshLayout -> {
                view.parent as SwipeRefreshLayout
            }
            is View -> {
                findRefresh(view.parent as View)
            }
            else -> null
        }
    }

    private fun setData(data: PagedList<Any>) {
        if (dataStatus == DataSourceFactory.Status.INITIALING || dataStatus == DataSourceFactory.Status.INITIAL_FAIL) {
            return
        }
        loader?.apply {
            list = data
            delegateAdapter.submitList(data)
        }
    }

    private fun pagedConfig(pageSize: Int): PagedList.Config {
        return PagedList.Config.Builder().setPageSize(5).setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize).setEnablePlaceholders(false).build()
    }

    private fun DelegateAdapter.getLastItem(): Any? = getItem(itemCount - 1)

    private fun setStatus(status: DataSourceFactory.Status) {
        "DataSourceStatus = $status".logi("DataBindingRecyclerView")
        dataStatus = status
        when (dataStatus) {
            DataSourceFactory.Status.INITIAL_SUCCESS -> post { scrollToPosition(0) }
            DataSourceFactory.Status.INITIAL_FAIL -> dataSourceFactory?.checkRetry()
            DataSourceFactory.Status.LOADING, DataSourceFactory.Status.LOAD_SUCCESS, DataSourceFactory.Status.LOAD_FAIL -> {
                delegateAdapter.apply {
                    if (getLastItem() != null) {
                        notifyItemChanged(itemCount - 1)
                    } else {
                        statusChangeNotify()
                    }
                }
            }
            DataSourceFactory.Status.COMPLETE -> {
                loader?.apply {
                    delegateAdapter.apply {
                        if (list.size >= pageSize) {
                            if (getLastItem() != null) {
                                notifyItemChanged(itemCount - 1)
                            } else {
                                statusChangeNotify()
                            }
                        } else {
                            statusChangeNotify()
                        }
                    }
                }
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
