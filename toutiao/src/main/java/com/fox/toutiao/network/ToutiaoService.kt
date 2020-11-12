package com.fox.toutiao.network

import com.fox.network.request.OriResponse
import com.fox.toutiao.bean.Banner
import com.fox.toutiao.bean.MultiNewsArticleBean
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by luyao
 * on 2018/3/13 14:33
 */
interface ToutiaoService {

    companion object {
        const val BASE_URL = "http://is.snssdk.com/"
    }

    //新版本的retrofit 内部处理了suspend函数 会自己开一个io协程 所以使用时不需要再手动开启协程
    @GET("/banner/json")
    suspend fun getBanner(): OriResponse<List<Banner>>

    @GET("http://is.snssdk.com/api/news/feed/v62/?iid=5034850950&device_id=6096495334&refer=1&count=20&aid=13")
    suspend fun getNewsArticle(
        @Query("category") category: String?,
        @Query("max_behot_time") maxBehotTime: String?
    ): OriResponse<List<MultiNewsArticleBean>>

    @GET("http://lf.snssdk.com/api/news/feed/v62/?iid=12507202490&device_id=37487219424&refer=1&count=20&aid=13")
    suspend fun getNewsArticle2(
        @Query("category") category: String?,
        @Query("max_behot_time") maxBehotTime: String?
    ): OriResponse<List<MultiNewsArticleBean>>
}