package com.fox.network.request

enum class RequestType {
    NETWORK_ONLY,  // 只从网络读取数据
    CACHE_OR_NETWORK,  //先缓存，无数据，从网络
    CACHE_ONLY,  // 只从缓存读取数据
    CACHE_NETWORK // 先从缓存拿，再从网络拿
}