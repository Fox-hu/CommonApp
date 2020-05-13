package com.fox.toutiao.ui.news

import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.NewsArticleRepository
import com.silver.fox.base.component.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsArticleViewModel(private val repository: NewsArticleRepository) : BaseViewModel() {

    var categoryId: String = ""

    fun getArticleList() {
        viewModelScope.launch(Dispatchers.Main) {
            val list = withContext(Dispatchers.IO) {
                repository.getArticleList()
            }
        }
    }
}