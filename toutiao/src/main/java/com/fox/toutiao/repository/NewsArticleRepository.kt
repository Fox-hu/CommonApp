package com.fox.toutiao.repository

import com.fox.network.Result
import com.fox.toutiao.R
import com.fox.toutiao.network.ToutiaoRetrofitClient
import com.fox.toutiao.ui.news.bean.NewsChannelBean
import com.silver.fox.base.component.repository.BaseRepository
import com.silver.fox.ext.getStringArray

class NewsArticleRepository : BaseRepository() {

    suspend fun getArticleList(): Result<List<String>> {
        return safeApiCall(call = { requestArticleList() }, errorMsg = "")
    }

    private suspend fun requestArticleList(): Result<List<String>> =
        executeResponse(ToutiaoRetrofitClient.service.getNewsArticle())

}