package com.fox.toutiao.ui.news

import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.HomeRepository
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.logi
import kotlinx.coroutines.launch

class NewsArticleViewModel(private val repository: HomeRepository) : BaseViewModel() {

    var categoryId: String = ""

    fun getBanners() {
        viewModelScope.launch {
            val banners = repository.getBanners()
            banners.data.toString().logi("home")
        }
    }
}