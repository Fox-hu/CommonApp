package com.silver.fox.recycleview

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.silver.fox.recycleview.adapter.Chain
import com.silver.fox.recycleview.adapter.VHolder
import com.silver.fox.recycleview.adapter.ViewTypes
import com.silver.fox.recycleview.datasource.DataSourceFactory
import com.silver.fox.recycleview.holder.DataBindingCell
import com.silver.fox.recycleview.pojo.FooterPresenterModel
import com.silver.fox.viewext.isMainThread
import java.util.*

/**
 * @Author fox.hu
 * @Date 2020/5/14 13:34
 */
class DelegateAdapter(diffCallback: DiffUtil.ItemCallback<Any>, builder: Builder) :
    PagedListAdapter<Any, RecyclerView.ViewHolder>(diffCallback) {

    private val viewTypes: ViewTypes
    private val recyclerView: DataBindingRecyclerView
    private val inflater: LayoutInflater
    private val handler: AdapterHandle = AdapterHandle()

    init {
        viewTypes = builder.viewTypes
        recyclerView = builder.recyclerView
        inflater = LayoutInflater.from(recyclerView.context)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun getItemViewType(position: Int): Int = getTypeOfIndex(position, getItem(position))

    public override fun getItem(position: Int): Any? {
        recyclerView.apply {
            if (position == 0 && super.getItemCount() == 0) {
                if (dataStatus == DataSourceFactory.Status.INITIAL_FAIL) {
                    return errorPresenterModel.clone()
                } else if (dataStatus == DataSourceFactory.Status.EMPTY) {
                    return emptyPresenterModel.clone()
                }
            } else if (position == super.getItemCount()) {
                if (dataStatus == DataSourceFactory.Status.LOADING
                    || dataStatus == DataSourceFactory.Status.LOAD_FAIL
                    || dataStatus == DataSourceFactory.Status.COMPLETE
                ) {
                    return footerPresenterModel.clone()
                }
            }
        }
        return super.getItem(position)
    }

    override fun getItemCount(): Int {
        val dataCount = super.getItemCount()
        recyclerView.apply {
            return if (dataCount == 0) {
                if (dataStatus == DataSourceFactory.Status.INITIAL_FAIL || dataStatus == DataSourceFactory.Status.EMPTY) {
                    1
                } else {
                    0
                }
            } else {
                if (dataStatus == DataSourceFactory.Status.LOADING || dataStatus == DataSourceFactory.Status.LOAD_FAIL || dataStatus == DataSourceFactory.Status.COMPLETE) {
                    dataCount + 1
                } else {
                    dataCount
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val vHolder = viewTypes.getItemView(viewType)
        return vHolder.onCreateViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(holder, position, Collections.emptyList())
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position)
        item?.let {
            val vHolder =
                viewTypes.getItemView(holder.itemViewType) as VHolder<Any, RecyclerView.ViewHolder>
            vHolder.onBindViewHolder(holder, item, position, payloads)
            if (holder is DataBindingCell.DataBindingViewHolder<*>) {
                if (item is FooterPresenterModel) {
                    holder.itemView.setOnClickListener {
                        if (recyclerView.dataStatus == DataSourceFactory.Status.LOAD_FAIL) {
                            recyclerView.dataSourceFactory.retry()
                        }
                    }
                }
            }
        }
    }

    fun statusChangeNotify() {
        if (isMainThread) {
            notifyDataSetChanged()
        } else {
            handler.notifyDataSetChanged()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getTypeOfIndex(position: Int, item: Any?): Int {
        if (item == null) {
            return -1
        }
        //获取储存Item类型class集合的索引
        val index = viewTypes.getClassIndexOf(item.javaClass)
        if (index != -1) {
            val chain: Chain<Any> = viewTypes.getChain(index) as Chain<Any>
            return index + chain.indexItem(position, item)
        }
        return -1
    }

    class Builder(
        val recyclerView: DataBindingRecyclerView,
        var viewTypes: ViewTypes = ViewTypes()
    ) {
        fun <T, VH : RecyclerView.ViewHolder> bind(
            clazz: Class<T>,
            vHolder: VHolder<T, VH>
        ): Builder {
            viewTypes.save(clazz, vHolder)
            return this
        }

        fun build(): DelegateAdapter {
            return DelegateAdapter(DIFF_CALLBACK, this)
        }
    }

    class AdapterHandle : Handler() {
        private val WHAT_NOTIFY_DATA_CHANGED = 1

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                WHAT_NOTIFY_DATA_CHANGED -> {
                    notifyDataSetChanged()
                }
            }
        }

        fun notifyDataSetChanged() {
            val msg = obtainMessage()
            msg.what = WHAT_NOTIFY_DATA_CHANGED
            msg.sendToTarget()
        }
    }
}