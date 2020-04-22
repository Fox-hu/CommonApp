package com.fox.toutiao.repository

import com.fox.network.Result
import com.fox.toutiao.network.ToutiaoRetrofitClient
import com.silver.fox.base.BaseRepository

class NewsRepository : BaseRepository() {

    suspend fun getArticleList(): Result<List<String>> {
        return safeApiCall(call = { requestArticleList() }, errorMsg = "")
    }

    private suspend fun requestArticleList(): Result<List<String>> =
        executeResponse(ToutiaoRetrofitClient.service.getNewsArticle())
}