package com.fox.toutiao.network

import androidx.lifecycle.LiveData
import com.fox.network.request.OriResponse
import com.fox.toutiao.bean.Banner
import retrofit2.http.GET


/**
 * Created by luyao
 * on 2018/3/13 14:33
 */
interface ToutiaoService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    //新版本的retrofit 内部处理了suspend函数 会自己开一个io协程 所以使用时不需要再手动开启协程
    @GET("/banner/json")
    suspend fun getBanner(): OriResponse<List<Banner>>
}