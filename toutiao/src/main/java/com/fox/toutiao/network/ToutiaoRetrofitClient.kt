package com.fox.toutiao.network

import com.fox.network.BaseRetrofitClient

object ToutiaoRetrofitClient : BaseRetrofitClient() {

    override val serviceMap: Map<Class<*>, Any>
        get() = mapOf(
            ToutiaoService::class.java to createService(
                ToutiaoService::class.java,
                ToutiaoService.BASE_URL
            )
        )

}