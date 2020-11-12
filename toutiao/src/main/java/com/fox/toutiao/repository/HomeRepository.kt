package com.fox.toutiao.repository

import androidx.lifecycle.MutableLiveData
import com.fox.network.request.OriResponse
import com.fox.network.request.OriResult
import com.fox.toutiao.bean.Banner
import com.fox.toutiao.bean.MultiNewsArticleBean
import com.fox.toutiao.network.ToutiaoRetrofitClient
import com.fox.toutiao.network.ToutiaoService
import com.fox.toutiao.network.WanService
import com.silver.fox.base.component.repository.BaseRepository

class HomeRepository : BaseRepository() {

    suspend fun getBanners(): MutableLiveData<OriResult<OriResponse<List<Banner>>>> {
        return executeResponse {
            ToutiaoRetrofitClient.getService<WanService>().getBanner()
        }
    }

    suspend fun getNewsArticle(categoryId: String): MutableLiveData<OriResult<OriResponse<List<MultiNewsArticleBean>>>> {
        return executeResponse {
            ToutiaoRetrofitClient.getService<ToutiaoService>().getNewsArticle2(
                categoryId,
                (System.currentTimeMillis() / 1000).toString()
            )
        }
    }
}