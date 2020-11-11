package com.fox.toutiao.ui.news

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.HomeRepository
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.logi
import kotlinx.coroutines.launch

class NewsArticleViewModel(private val repository: HomeRepository) : BaseViewModel() {

    private lateinit var categoryId: String

//    fun getBanners(
//        successBlock: (suspend CoroutineScope.() -> Unit)? = null,
//        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
//    ) {
//        viewModelScope.launch {
//            repository.getBanners().observeForever {
//                "status  = ${it.status.name} ,data = ${it.data?.data.toString()}".logi()
//            }
//        }
//    }

    override fun onFragmentArguments(bundle: Bundle?) {
        super.onFragmentArguments(bundle)
        categoryId = bundle?.getString("categoryId") ?: ""
    }

    override fun onFragmentFirstVisible() {
        "categoryId = $categoryId".logi("NewsArticleViewModel")
        viewModelScope.launch {
            repository.getNewsArticle(categoryId).observeForever {
                "status  = ${it.status.name} ,data = ${it.data?.data.toString()},msg = ${it.message}".logi()
            }
        }
    }
}