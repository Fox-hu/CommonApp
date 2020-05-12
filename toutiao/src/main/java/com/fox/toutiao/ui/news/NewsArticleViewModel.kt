package com.fox.toutiao.ui.news

import android.app.Application
import com.fox.toutiao.repository.NewsRepository
import com.silver.fox.base.BaseViewModel
import com.silver.fox.common.InitApp

class NewsArticleViewModel(val repository: NewsRepository, app: Application) : BaseViewModel(app) {

}