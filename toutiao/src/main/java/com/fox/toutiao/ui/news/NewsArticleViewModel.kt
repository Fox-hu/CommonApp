package com.fox.toutiao.ui.news

import android.os.Bundle
import android.os.Handler
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import com.fox.toutiao.bean.MultiNewsArticleDataBean
import com.fox.toutiao.repository.HomeRepository
import com.google.gson.Gson
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NewsArticleViewModel(private val repository: HomeRepository) : BaseViewModel() {


    private lateinit var categoryId: String
    val pageStatus = ObservableField<OriResult.Status>()

    fun getBanners(
        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        viewModelScope.launch {
            repository.getBanners().observeForever {
                "status  = ${it.status.name} ,data = ${it.data?.data.toString()}".logi()
            }
        }
    }

    override fun onFragmentArguments(bundle: Bundle?) {
        super.onFragmentArguments(bundle)
        categoryId = bundle?.getString("categoryId") ?: ""
    }

    override fun onFragmentFirstVisible() {
        viewModelScope.launch {
            val newsArticle = repository.getNewsArticle(categoryId)
            Transformations.map(newsArticle) {
                val list = it.data?.data?.map { bean ->
                    Gson().fromJson(bean.content, MultiNewsArticleDataBean::class.java)
                }
                it.copyIgnoreData(OriResponse(data = list))
            }.observeForever {
                "state = ${it.status.name}".logi("DataBindingRefreshLayout")
                pageStatus.set(it.status)
            }
        }

//        val test = MutableLiveData<OriResult.Status>()
//        test.postValue(OriResult.Status.LOADING)
//
//        test.observeForever {
//            "state = ${it.name}".logi("DataBindingRefreshLayout")
//        }
//        test.postValue(OriResult.Status.ACTION_SUCCESS)
    }

}