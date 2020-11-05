package com.fox.toutiao.ui.news

import androidx.fragment.app.Fragment
import com.fox.toutiao.repository.NewsRepository
import com.fox.toutiao.ui.news.bean.NewsChannelBean
import com.silver.fox.base.component.viewmodel.BaseViewModel

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