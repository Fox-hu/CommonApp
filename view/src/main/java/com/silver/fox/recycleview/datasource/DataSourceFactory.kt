package com.silver.fox.recycleview.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

/**
 * @Author fox.hu
 * @Date 2020/5/15 13:43
 */
class DataSourceFactory(val dataLoader: DataLoader) : DataSource.Factory<Int, Any>() {
    val status = MutableLiveData<Status>()
    var retry: Runnable? = null

    override fun create(): DataSource<Int, Any> {
        return object : PageKeyedDataSource<Int, Any>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, Any>
            ) {
                PagingExecutor.MAIN_THREAD_EXECUTOR.execute {
                    val pageAt = 1
                    status.value = Status.INITIALING
                    dataLoader.fetchData(pageAt, params.requestedLoadSize).observeForever {
                        when {
                            it == null -> {
                                status.value = Status.INITIAL_FAIL
                            }
                            it.isEmpty() -> {
                                callback.onResult(ArrayList(it), null, null)
                                status.value = Status.EMPTY
                            }
                            it.size < params.requestedLoadSize -> {
                                callback.onResult(ArrayList(it), null, null)
                                status.value = Status.INITIAL_SUCCESS
                                status.value = Status.COMPLETE
                            }

                            else -> {
                                callback.onResult(ArrayList(it), null, pageAt + 1)
                                status.value = Status.INITIAL_SUCCESS
                            }
                        }
                    }
                }
            }

            override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
                PagingExecutor.MAIN_THREAD_EXECUTOR.execute {
                    val pageAt = params.key
                    status.value = Status.LOADING
                    dataLoader.fetchData(pageAt, params.requestedLoadSize).observeForever {
                        when {
                            it == null -> {
                                status.value = Status.LOAD_FAIL
                                retry = Runnable { loadAfter(params, callback) }
                            }
                            it.size < params.requestedLoadSize -> {
                                callback.onResult(ArrayList(it), null)
                                status.value = Status.COMPLETE
                            }
                            else -> {
                                callback.onResult(ArrayList(it), pageAt + 1)
                                status.value = Status.LOAD_SUCCESS
                            }
                        }
                    }
                }
            }

            override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Any>) {
            }

        }
    }

    fun retry() {
        retry?.run {
            PagingExecutor.MAIN_THREAD_EXECUTOR.execute(this)
        }
    }


    enum class Status {
        NONE, EMPTY, INITIALING, INITIAL_FAIL, INITIAL_SUCCESS, LOADING, LOAD_FAIL, LOAD_SUCCESS, COMPLETE
    }
}