package com.fox.toutiao.ui.news

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.fox.toutiao.repository.NewsRepository
import com.silver.fox.base.BaseViewModel
import com.silver.fox.common.InitApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(val repository: NewsRepository) : BaseViewModel() {

    fun getArticleList() {
        viewModelScope.launch(Dispatchers.Main) {
            val list = withContext(Dispatchers.IO) {
                repository.getArticleList()
            }
        }
    }
}