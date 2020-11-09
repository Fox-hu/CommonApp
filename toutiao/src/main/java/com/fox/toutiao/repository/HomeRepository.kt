package com.fox.toutiao.repository

import com.fox.network.request.OriResult
import com.fox.toutiao.bean.Banner
import com.fox.toutiao.network.WanRetrofitClient
import com.silver.fox.base.component.repository.BaseRepository

class HomeRepository : BaseRepository() {

    suspend fun getBanners(): OriResult<List<Banner>> {
        return safeApiCall(call = {
            executeResponse(WanRetrofitClient.service.getBanner())
        }, errorMsg = "")
    }
}