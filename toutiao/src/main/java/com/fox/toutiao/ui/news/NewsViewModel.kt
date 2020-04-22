package com.fox.toutiao.ui.news

import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.NewsRepository
import com.silver.fox.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NewsViewModel(val repository: NewsRepository) : BaseViewModel() {

    fun getArticleList() {
//        viewModelScope.launch(Dispatchers.Main) {
//            val articleList = repository.getArticleList()
//
//        }
        viewModelScope.launch(Dispatchers.Main){
            val list = async { repository.getArticleList() }
        }
    }
}