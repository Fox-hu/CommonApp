package com.fox.toutiao.ui.news

import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.fox.toutiao.R
import com.fox.toutiao.repository.NewsRepository
import com.fox.toutiao.ui.news.bean.NewsChannelBean
import com.silver.fox.base.component.viewmodel.BaseViewModel
import com.silver.fox.ext.getString
import com.silver.fox.ext.getStringArray
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(private val repository: NewsRepository) : BaseViewModel() {

    private val newsCategoryList: List<NewsChannelBean> by lazy {
        repository.getNewsCategoryList()
    }

    val fragmentList: List<Fragment> by lazy {
        newsCategoryList.map {
            NewsArticleFragment(it.channelId)
        }.toList()
    }
    val titleList: List<String> by lazy {
        newsCategoryList.map { it.channelName }.toList()
    }

}