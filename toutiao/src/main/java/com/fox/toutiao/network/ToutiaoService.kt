package com.fox.toutiao.network

import com.fox.network.Response
import retrofit2.http.GET


/**
 * Created by luyao
 * on 2018/3/13 14:33
 */
interface ToutiaoService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @GET("/article/list/")
    suspend fun getNewsArticle(): Response<List<String>>
}