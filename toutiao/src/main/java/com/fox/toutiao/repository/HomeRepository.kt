package com.fox.toutiao.repository

import androidx.lifecycle.MutableLiveData
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import com.fox.toutiao.bean.Banner
import com.fox.toutiao.network.ToutiaoRetrofitClient
import com.fox.toutiao.network.ToutiaoService
import com.silver.fox.base.component.repository.BaseRepository

class HomeRepository : BaseRepository() {

    suspend fun getBanners(): MutableLiveData<OriResult<OriResponse<List<Banner>>>> {
        return executeResponse {
            ToutiaoRetrofitClient.getService<ToutiaoService>().getBanner()
        }
    }
}