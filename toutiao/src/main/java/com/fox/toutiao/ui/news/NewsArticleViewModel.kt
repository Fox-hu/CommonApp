package com.fox.toutiao.ui.news

import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.HomeRepository
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NewsArticleViewModel(private val repository: HomeRepository) : BaseViewModel() {

    var categoryId: String = ""

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

    override fun onFragmentFirstVisible() {

    }

}